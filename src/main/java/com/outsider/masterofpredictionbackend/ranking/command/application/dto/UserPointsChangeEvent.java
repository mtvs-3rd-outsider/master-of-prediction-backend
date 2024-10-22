package com.outsider.masterofpredictionbackend.ranking.command.application.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPointsChangeEvent {
    private Long userId;
    private BigDecimal newPoints;
}