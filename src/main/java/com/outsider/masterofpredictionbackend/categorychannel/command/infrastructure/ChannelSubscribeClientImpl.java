package com.outsider.masterofpredictionbackend.categorychannel.command.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.service.ChannelSubscribeClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.UpdateFollowMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.UPDATE_CHANNEL_SUBSCRIBE;

@Service
public class ChannelSubscribeClientImpl implements ChannelSubscribeClient {
    @Override
    public void publish(Long userId, Long channelId, boolean isUserChannel) {
        ChannelSubscribeRequestDTO message = new ChannelSubscribeRequestDTO(userId, channelId, isUserChannel);

        String messageJson = null;
        try {
            messageJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message to JSON", e);
        }

        // Kafka로 메시지 전송
        kafkaTemplate.send(UPDATE_CHANNEL_SUBSCRIBE, messageJson);
        System.out.println("Sent follower update message to Kafka: " + messageJson);
    }

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ChannelSubscribeClientImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }



}
