package com.outsider.masterofpredictionbackend.bettingorder.command.domain.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface UserPoint {
    BigDecimal find(Long userId);
    void pointUpdate(Long userId, BigDecimal point);
}
