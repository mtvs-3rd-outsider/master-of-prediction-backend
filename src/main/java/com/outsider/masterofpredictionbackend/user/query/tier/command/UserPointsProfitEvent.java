package com.outsider.masterofpredictionbackend.user.query.tier.command;


import lombok.Data;

@Data
public class UserPointsProfitEvent {
    private Long userId;
    private int profitability;
}