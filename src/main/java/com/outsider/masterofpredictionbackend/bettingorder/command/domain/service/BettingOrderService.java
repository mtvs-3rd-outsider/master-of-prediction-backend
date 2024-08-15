package com.outsider.masterofpredictionbackend.bettingorder.command.domain.service;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.repository.BettingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BettingOrderService {

    private final BettingOrderRepository bettingOrderRepository;

    @Autowired
    public BettingOrderService(BettingOrderRepository bettingOrderRepository) {
        this.bettingOrderRepository = bettingOrderRepository;
    }

    public BettingOrder save(BettingOrder bettingOrder) {
        return bettingOrderRepository.save(bettingOrder);
    }

    public BettingOrder findById(Long id) {
        return bettingOrderRepository.findById(id).orElse(null);
    }
}
