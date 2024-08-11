package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BettingProductOptionService {

    private final BettingProductOptionRepository optionRepository;

    @Autowired
    public BettingProductOptionService(BettingProductOptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public void save(BettingProductOption itemAggregate) {
        optionRepository.save(itemAggregate);
    }

    public void saveAll(List<BettingProductOption> items){
        optionRepository.saveAll(items);
    }

    public void delete(BettingProductOption itemAggregate) {
        optionRepository.delete(itemAggregate);
    }
}
