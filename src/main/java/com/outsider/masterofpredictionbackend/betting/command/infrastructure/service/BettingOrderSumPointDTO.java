package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingOrderSumPointDTO {
    Long bettingOptionId;
    Long userId;
    BigDecimal orderPoint;
}
