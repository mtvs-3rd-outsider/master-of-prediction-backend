package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BettingProductService {

    private final BettingProductRepository bettingRepository;

    @Autowired
    public BettingProductService(BettingProductRepository bettingRepository) {
        this.bettingRepository = bettingRepository;
    }

    public void save(BettingProduct bettingAggregate) {
        bettingRepository.save(bettingAggregate);
    }

    public void delete(BettingProduct bettingAggregate) {
        bettingRepository.delete(bettingAggregate);
    }
}
