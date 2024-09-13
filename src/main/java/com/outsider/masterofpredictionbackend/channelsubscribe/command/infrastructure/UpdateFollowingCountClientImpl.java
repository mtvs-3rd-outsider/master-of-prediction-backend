package com.outsider.masterofpredictionbackend.channelsubscribe.command.infrastructure;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.UpdateFollowMessage;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowingCountClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.UPDATE_FOLLOWING_COUNT_TOPIC;

@Service
public class UpdateFollowingCountClientImpl implements UpdateFollowingCountClient {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UpdateFollowingCountClientImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(Long channelId, boolean isUserChannel, boolean isPlus) {
        // 메시지 생성
        UpdateFollowMessage message = new UpdateFollowMessage(channelId, isUserChannel, isPlus);

        String messageJson = null;
        try {
            messageJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message to JSON", e);
        }

        // Kafka로 메시지 전송
        kafkaTemplate.send(UPDATE_FOLLOWING_COUNT_TOPIC, messageJson);
        System.out.println("Sent message to Kafka: " + messageJson);
    }

}
