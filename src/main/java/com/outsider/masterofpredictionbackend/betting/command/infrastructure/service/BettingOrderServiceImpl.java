package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingOrderService;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.repository.BettingOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BettingOrderServiceImpl implements BettingOrderService {

    private final BettingOrderRepository bettingOrderRepository;

    public BettingOrderServiceImpl(BettingOrderRepository bettingOrderRepository) {
        this.bettingOrderRepository = bettingOrderRepository;
    }

    @Override
    public List<BettingOrderSumPointDTO> calculateUserOrderPointSumByProductId(Long productId, Long bettingOptionId) {
        List<Object[]> info = bettingOrderRepository.calculateUserOrderPointSumByProductId(productId);

        List<BettingOrderSumPointDTO> bettingOrderSumPointDTOS = new ArrayList<>();
        for (Object[] objects : info) {
            BettingOrderSumPointDTO bettingOrderSumPointDTO = new BettingOrderSumPointDTO();

            bettingOrderSumPointDTO.setBettingOptionId((Long) objects[0]);
            bettingOrderSumPointDTO.setUserId((Long) objects[1]);
            bettingOrderSumPointDTO.setOrderPoint((BigDecimal) objects[2]);
            bettingOrderSumPointDTOS.add(bettingOrderSumPointDTO);
        }
        return bettingOrderSumPointDTOS;

    }

    @Override
    public List<Long> findUserIdsByProductId(Long productId) {
        return bettingOrderRepository.findUserIdsByBettingId(productId);
    }

}
