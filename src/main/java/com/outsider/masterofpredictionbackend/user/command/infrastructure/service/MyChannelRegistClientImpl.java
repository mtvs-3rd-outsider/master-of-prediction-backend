package com.outsider.masterofpredictionbackend.user.command.infrastructure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.service.MyChanneRegistClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_CREATE_TOPIC;
import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_UPDATE_TOPIC;

@Slf4j
@Service
public class MyChannelRegistClientImpl implements MyChanneRegistClient {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MyChannelRegistClientImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(Long userId)  {
        // MyChannelInfoUpdateRequestDTO 객체를 JSON 문자열로 변환
        kafkaTemplate.send(MY_CHANNEL_CREATE_TOPIC, userId.toString());
        log.info("Sent message to Kafka: {} {}","MY_CHANNEL_CREATE_TOPIC" ,userId);

    }
}
