package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingKafkaService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BettingKafkaServiceImpl implements BettingKafkaService {

    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;


    public BettingKafkaServiceImpl(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendSettlementEvent(List<User> list) {
        for (User user : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("newPoints", user.getPoints());
            try {
                kafkaTemplate.send("settlement", objectMapper.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                log.error("error kafka sending settlement event: {}", e.getMessage());
                throw new RuntimeException();
            }
        }
    }
}
