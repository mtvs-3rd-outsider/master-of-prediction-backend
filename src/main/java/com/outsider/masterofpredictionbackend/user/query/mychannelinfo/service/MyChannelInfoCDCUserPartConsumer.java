package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

//TODO: 카프카 listener 변경하고, 상속 받고 생성자 만들고 아래 TODO쪽도 변경하면 끝
@Service
public class MyChannelInfoCDCUserPartConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MyChannelInfoCDCUserPartConsumer.class);

    private final MyChannelInfoRepository repository;
    private final UserForMyChannelInfoMapper userForMyChannelInfoMapper;
    private final ObjectMapper objectMapper;

    public MyChannelInfoCDCUserPartConsumer(MyChannelInfoRepository repository,
                                            UserForMyChannelInfoMapper userForMyChannelInfoMapper,
                                            ObjectMapper objectMapper) {
        this.repository = repository;
        this.userForMyChannelInfoMapper = userForMyChannelInfoMapper;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.user", groupId = "my-channel-info-user-group")
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

    public void handleCreateOrUpdate(JsonNode jsonNode) {
        try {
            // JSON 데이터를 User 객체로 변환
            User user = objectMapper.treeToValue(jsonNode, User.class);

            // User 데이터를 MyChannelInfoQueryModel로 변환
            MyChannelInfoQueryModel newData = userForMyChannelInfoMapper.toQueryModel(user);

            // 기존 데이터 조회
            Long userId = newData.getUserId();
            MyChannelInfoQueryModel existingData = repository.findById(userId).orElse(null);

            if (existingData == null) {
                repository.save(newData);
            } else {
                // 기존 데이터와 변환된 데이터를 병합
                userForMyChannelInfoMapper.updateFromQueryModel(newData, existingData);
                repository.save(existingData);
            }
        } catch (Exception e) {
            logger.error("Error while processing create/update for JSON: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    public void handleDelete(JsonNode jsonNode) {
        try {
            Long userId = jsonNode.get("user_id").asLong();
            repository.deleteById(userId);
        } catch (Exception e) {
            logger.error("Error while processing delete for JSON: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    private void retryProcessing(ConsumerRecord<String, String> record, Acknowledgment ack) {
        logger.warn("Retry logic is not implemented yet for record: {}", record);
        ack.nack(Duration.ofSeconds(1)); // 1초 후 재처리
    }
}
