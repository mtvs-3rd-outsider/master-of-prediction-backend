package com.outsider.masterofpredictionbackend.notification.command.application.dto;


import lombok.Data;

@Data
public class UserPointsProfitEvent {
    private Long userId;
    private int profitability;
}