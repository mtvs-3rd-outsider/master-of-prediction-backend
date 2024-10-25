package com.outsider.masterofpredictionbackend.bettingorder.command.domain.service;

public interface BettingProductValidator {
    boolean isBettingProductExists(Long productId);
    boolean validateProductExistenceAndStatus(Long productId);
}
