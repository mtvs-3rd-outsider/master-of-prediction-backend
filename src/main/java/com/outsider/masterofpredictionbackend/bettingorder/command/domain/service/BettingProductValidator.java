package com.outsider.masterofpredictionbackend.bettingorder.command.domain.service;

public interface BettingProductValidator {
    boolean validateProductExistenceAndStatus(Long productId);
}
