package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.infrastructure.service.BettingOrderSumPointDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BettingOrderService {
    List<BettingOrderSumPointDTO> calculateUserOrderPointSumByProductId(Long productId, Long bettingOptionId);

    List<Long> findUserIdsByProductId(Long productId);
}
