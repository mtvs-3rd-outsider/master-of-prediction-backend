package com.outsider.masterofpredictionbackend.channelsubscribe.query.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.SubscriptionEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Slf4j
public class SubscriptionCDCEventConsumer {

    private final SubscriptionCDCEventHandler eventHandler;
    private final ObjectMapper objectMapper;

    public SubscriptionCDCEventConsumer(SubscriptionCDCEventHandler eventHandler, ObjectMapper objectMapper) {
        this.eventHandler = eventHandler;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.channel_subscribe", groupId = "subscription-group")
    @Transactional
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String consumedValue = record.value();

        if (consumedValue == null) {
            log.error("Consumed record value is null for record: {}", record);
            return;
        }

        try {
            // Kafka 메시지를 JSON으로 변환
            JsonNode jsonNode = objectMapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            String operation = payload.get("op").asText().substring(0, 1); // "c", "u", "d" 등의 연산 구분
            JsonNode after = payload.path("after");  // 변경 후 데이터
            JsonNode before = payload.path("before"); // 변경 전 데이터

            // 각 연산에 따른 처리
            switch (operation) {
                case "c":  // create
                case "u":  // update
                    handleCreateOrUpdate(after);
                    break;
                case "d":  // delete
                    handleDelete(before);
                    break;
                default:
                    log.warn("Unknown operation type: {}", operation);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing Kafka record", e);
            ack.nack(Duration.ofSeconds(1));
        }
    }

    private void handleCreateOrUpdate(JsonNode after) throws JsonProcessingException {
        if (after != null && !after.isNull()) {
            SubscriptionEvent event=  objectMapper.treeToValue(after, SubscriptionEvent.class);
            // 이벤트 핸들링
            eventHandler.handleEvent(event);
        }
    }

    private void handleDelete(JsonNode before) throws JsonProcessingException {
        if (before != null && !before.isNull()) {
            SubscriptionEvent event=  objectMapper.treeToValue(before, SubscriptionEvent.class);
            event.setSubscribed(false); // 구독 해제 시 false로 설정
            eventHandler.handleEvent(event);
        }
    }
}
