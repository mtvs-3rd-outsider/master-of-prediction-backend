package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;

@Service
@Slf4j
@EnableRetry // Spring Retry 활성화
public class MyChannelInfoCDCUserPartConsumer {


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
        try {
            processRecord(record);
        } catch (Exception e) {
            log.error("Error processing record after retries: {}", record, e);
        } finally {
            ack.acknowledge(); // 항상 메시지 acknowledge
        }
    }

    @Retryable(
            maxAttempts = 5, // 최대 5번 재시도
            backoff = @Backoff(delay = 2000, multiplier = 2) // 초기 2초 대기, 점진적 증가
    )
    public void processRecord(ConsumerRecord<String, String> record) throws Exception {
        String consumedValue = record.value();
        if (consumedValue == null) {
            log.error("Consumed record value is null for record: {}", record);
            throw new IllegalArgumentException("Consumed value is null");
        }

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
    }

    @Recover
    public void recover(Exception e, ConsumerRecord<String, String> record) {
        log.error("Recovering from error after retries. Failed record: {}", record, e);
        // 실패한 메시지에 대한 대체 처리 로직
        // 예: 실패한 메시지를 Dead Letter Queue(DLQ)로 전송
        sendToDeadLetterQueue(record, e.getMessage());
    }

    public void handleCreateOrUpdate(JsonNode jsonNode) {
        try {
            User user = objectMapper.treeToValue(jsonNode, User.class);
            MyChannelInfoQueryModel newData = userForMyChannelInfoMapper.toQueryModel(user);
            Long userId = newData.getUserId();
            MyChannelInfoQueryModel existingData = repository.findById(userId).orElse(null);

            if (existingData == null) {
                repository.save(newData);
            } else {
                userForMyChannelInfoMapper.updateFromQueryModel(newData, existingData);
                repository.save(existingData);
            }
        } catch (Exception e) {
            log.error("Error processing create/update for JSON: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    public void handleDelete(JsonNode jsonNode) {
        try {
            Long userId = jsonNode.get("user_id").asLong();
            repository.deleteById(userId);
        } catch (Exception e) {
            log.error("Error processing delete for JSON: {}", jsonNode, e);
            throw new RuntimeException(e);
        }
    }

    private void sendToDeadLetterQueue(ConsumerRecord<String, String> record, String errorMessage) {
        // 실패한 메시지를 Dead Letter Queue로 전송하는 로직 구현
        log.info("Sending record to Dead Letter Queue: {}, Error: {}", record, errorMessage);
        // 실제 구현 예시: KafkaTemplate 또는 외부 서비스 호출
    }
}

