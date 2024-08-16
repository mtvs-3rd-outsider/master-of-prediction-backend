package com.outsider.masterofpredictionbackend.bettingorder.command.infrastructure;

import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductService;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.BettingProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BettingProductValidatorImpl implements BettingProductValidator {

    private final BettingProductService bettingProductService;

    @Autowired
    public BettingProductValidatorImpl(BettingProductService bettingProductService) {
        this.bettingProductService = bettingProductService;
    }

    @Override
    public boolean validateProductExistenceAndStatus(Long productId) {
        return bettingProductService.validateProductExistenceAndStatus(productId);
    }
}
