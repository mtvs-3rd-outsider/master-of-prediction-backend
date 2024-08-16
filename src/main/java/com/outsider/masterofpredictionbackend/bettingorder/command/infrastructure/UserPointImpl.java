package com.outsider.masterofpredictionbackend.bettingorder.command.infrastructure;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.UserPoint;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserPointImpl implements UserPoint {
    @Override
    public BigDecimal find(Long userId) {
        return null;
    }

    @Override
    public void pointUpdate(Long userId, BigDecimal point) {

    }
}
