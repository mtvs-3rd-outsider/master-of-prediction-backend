package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BettingOrderStatisticsDTO {
    
    private LocalDate orderDate;

    private LocalTime orderTime;

    private Long bettingOptionId;

    private BigDecimal totalPoints;

    private int Ratio;

    public BettingOrderStatisticsDTO(LocalDate orderDate, LocalTime orderTime, Long bettingOptionId, BigDecimal totalPoints) {
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.bettingOptionId = bettingOptionId;
        this.totalPoints = totalPoints;
    }
}
