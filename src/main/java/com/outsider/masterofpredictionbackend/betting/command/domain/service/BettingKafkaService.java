package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface BettingKafkaService {
    void sendSettlementEvent(List<User> list);
}
