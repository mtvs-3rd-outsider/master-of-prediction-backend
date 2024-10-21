package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface BettingKafkaService{
    void sendSettlementEvent(Long userId, BigDecimal newPoints);
}