package com.outsider.masterofpredictionbackend.user.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.service.MyChannelInfoUpdateClient;
import org.springframework.stereotype.Component;

import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_UPDATE_TOPIC;

@Service
public class MyChannelInfoUpdateClientImpl implements MyChannelInfoUpdateClient {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public MyChannelInfoUpdateClientImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(MyChannelInfoUpdateRequestDTO dto)  {
            // MyChannelInfoUpdateRequestDTO 객체를 JSON 문자열로 변환
        String message = null;
        try {
            message = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // Kafka로 JSON 문자열 전송
            kafkaTemplate.send(MY_CHANNEL_UPDATE_TOPIC, message);
            System.out.println("Sent message to Kafka: " + message);

    }
}
