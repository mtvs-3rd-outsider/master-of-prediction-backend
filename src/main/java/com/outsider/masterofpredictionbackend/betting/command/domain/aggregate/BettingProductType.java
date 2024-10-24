package com.outsider.masterofpredictionbackend.betting.command.domain.aggregate;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum BettingProductType {
    NAVER("naver"),
    USER("user");

    private final String type;

    BettingProductType(String type) {
        this.type = type;
    }
}
