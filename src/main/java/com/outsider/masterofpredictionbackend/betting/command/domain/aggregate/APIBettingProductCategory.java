package com.outsider.masterofpredictionbackend.betting.command.domain.aggregate;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum APIBettingProductCategory {
    KOREA_FOOTBALL("kfootball"),
    GLOBAL_FOOTBALL("gfootball"),
    KOREA_BASEBALL("kbaseball"),
    GLOBAL_BASEBALL("gbaseball"),
    BASKETBALL("basketball");

    private final String category;

    APIBettingProductCategory(String category) {
        this.category = category;
    }
}
