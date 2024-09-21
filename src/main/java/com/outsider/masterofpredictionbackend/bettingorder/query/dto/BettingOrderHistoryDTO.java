package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingOrderHistoryDTO {

    private Long bettingOptionId;

    private BigDecimal point;
}
