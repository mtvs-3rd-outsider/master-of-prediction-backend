package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsTimeDTO {

    private LocalTime timeSlot;
    private BigDecimal totalPoints;
    private int ratio;

    public StatisticsTimeDTO(LocalTime timeSlot, BigDecimal totalPoints) {
        this.timeSlot = timeSlot;
        this.totalPoints = totalPoints;
    }
}