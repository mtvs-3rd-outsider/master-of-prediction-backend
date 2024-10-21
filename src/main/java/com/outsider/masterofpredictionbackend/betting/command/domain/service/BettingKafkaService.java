package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BettingKafkaService {
    void sendSettlementEvent(List<User> list);
}
