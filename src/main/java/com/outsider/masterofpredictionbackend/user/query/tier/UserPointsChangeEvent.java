package com.outsider.masterofpredictionbackend.user.query.tier;


import lombok.Data;

@Data
public class UserPointsChangeEvent {
    private Long userId;
    private int newPoints;
}