package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.event.ChannelSubscriptionEvent;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.event.ChannelSubscriptionEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelSubscriptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public void sendValidationRequestUserPart(ChannelSubscribeRequestDTO dto) throws JsonProcessingException {
        ChannelSubscriptionEvent event = new ChannelSubscriptionEvent(
                dto,
                false
        );
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("user-channel-validation-request-user-part", eventJson);
    }

    public void sendValidationRequestChannelPart(ChannelSubscribeRequestDTO dto) throws JsonProcessingException {
        ChannelSubscriptionEvent event = new ChannelSubscriptionEvent(
                dto,
                false
        );
        String eventJson = objectMapper.writeValueAsString(event);
        if(dto.getIsUserChannel())
        {
            kafkaTemplate.send("user-channel-validation-request-channel-part", eventJson);
        }else
        {
            kafkaTemplate.send("category-channel-validation-request-channel-part", eventJson);
        }
    }
}
