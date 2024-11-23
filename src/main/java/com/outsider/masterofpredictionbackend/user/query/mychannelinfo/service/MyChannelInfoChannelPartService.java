package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import com.outsider.masterofpredictionbackend.util.GenericService;
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

@Service
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
        System.out.printf("Received message: %s, From partition: %d, With offset: %d, From topic: %s%n",
                record.value(), record.partition(), record.offset(), record.topic());
        String consumedValue = record.value();

        if (consumedValue == null) {
            logger.error("Consumed record value is null for record: {}", record);
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
                    logger.warn("Unknown operation type: {}", operation);
            }
            ack.acknowledge();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while consuming record: {}", record, e);
            retryProcessing(record, ack);
        }
    }

    private void handleCreateOrUpdate(JsonNode jsonNode) {
        try {
            if (jsonNode.isNull()) {
                logger.warn("No data found in 'after' field for update/create operation.");
                return;
            }

            // JSON 데이터를 MyChannel 객체로 변환
            MyChannel myChannel = objectMapper.treeToValue(jsonNode, MyChannel.class);

            // MyChannel 객체를 MyChannelInfoQueryModel로 매핑
            MyChannelInfoQueryModel newData = myChannelMapper.toQueryModel(myChannel);

            // 기존 데이터 조회
            Long channelId = newData.getUserId();
            MyChannelInfoQueryModel existingData = repository.findById(channelId).orElse(null);

            if (existingData == null) {
                // 새로운 데이터 저장
                repository.save(newData);
            } else {
                // 기존 데이터와 병합
                myChannelMapper.updateFromQueryModel(newData, existingData);
                repository.save(existingData);
            }
        } catch (Exception e) {
            logger.error("Error while processing create/update: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    private void handleDelete(JsonNode jsonNode) {
        try {
            Long channelId = jsonNode.get("channel_id").asLong();
            repository.deleteById(channelId);
        } catch (Exception e) {
            logger.error("Error while processing delete: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    private void retryProcessing(ConsumerRecord<String, String> record, Acknowledgment ack) {
        logger.warn("Retry logic is not implemented yet for record: {}", record);
        ack.nack(Duration.ofSeconds(1)); // 1초 대기 후 재처리
    }
}
