package com.outsider.masterofpredictionbackend.user.query.tier.command;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPointsChangeEvent {
    private Long userId;
    private BigDecimal newPoints;
}