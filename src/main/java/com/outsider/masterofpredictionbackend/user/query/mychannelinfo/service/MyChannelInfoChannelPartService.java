package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import com.outsider.masterofpredictionbackend.util.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;


import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableRetry // Spring Retry 활성화
@Slf4j
public class MyChannelInfoChannelPartService {

    private static final Logger logger = LoggerFactory.getLogger(MyChannelInfoChannelPartService.class);
    private final MyChannelInfoRepository repository;
    private final ObjectMapper objectMapper;
    private final MyChannelMapper myChannelMapper;

    public MyChannelInfoChannelPartService(MyChannelInfoRepository repository,
                                           ObjectMapper objectMapper,
                                           MyChannelMapper myChannelMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.myChannelMapper = myChannelMapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.my_channel", groupId = "my-channel-info-channel-group")
    @Transactional
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("Received message: {}, From partition: {}, With offset: {}, From topic: {}",
                record.value(), record.partition(), record.offset(), record.topic());
        String consumedValue = record.value();

        if (consumedValue == null) {
            log.error("Consumed record value is null for record: {}", record);
            ack.acknowledge();
            return;
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            String operation = payload.get("op").asText().substring(0, 1);
            JsonNode after = payload.path("after");
            JsonNode before = payload.path("before");

            switch (operation) {
                case "c":
                case "u":
                    handleCreateOrUpdate(after);
                    break;
                case "d":
                    handleDelete(before);
                    break;
                default:
                    log.warn("Unknown operation type: {}", operation);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error occurred while consuming record: {}", record, e);
            ack.acknowledge();
        }
    }

    @Retryable(
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2) // 재시도 간격 점진적 증가
    )
    private void handleCreateOrUpdate(JsonNode jsonNode) {
        try {
            if (jsonNode == null || jsonNode.isNull()) {
                log.warn("No data found in 'after' field for update/create operation.");
                return;
            }

            MyChannel myChannel = objectMapper.treeToValue(jsonNode, MyChannel.class);
            MyChannelInfoQueryModel newData = myChannelMapper.toQueryModel(myChannel);

            Long channelId = newData.getUserId();
            MyChannelInfoQueryModel existingData = repository.findById(channelId).orElse(null);

            if (existingData == null) {
                repository.save(newData);
                log.info("New data saved for channelId {}: {}", channelId, newData);
            } else {
                myChannelMapper.updateFromQueryModel(newData, existingData);
                repository.save(existingData);
                log.info("Existing data updated for channelId {}: {}", channelId, existingData);
            }
        } catch (Exception e) {
            log.error("Error while processing create/update: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    @Retryable(
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2) // 재시도 간격 점진적 증가
    )
    private void handleDelete(JsonNode jsonNode) {
        try {
            if (jsonNode == null || jsonNode.isNull()) {
                log.warn("No data found in 'before' field for delete operation.");
                return;
            }

            Long channelId = jsonNode.get("channel_id").asLong();
            repository.deleteById(channelId);
            log.info("Data deleted for channelId {}", channelId);
        } catch (Exception e) {
            log.error("Error while processing delete: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    @Recover
    public void recover(RuntimeException e, JsonNode jsonNode) {
        log.error("Recovering from error after retries. Failed data: {}", jsonNode, e);
        // 대체 로직 예: 실패 데이터 저장 또는 알림 전송
    }
}
