package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDateTimeDTO {
    private LocalDateTime timeSlot;
    private BigDecimal totalPoints;
    private Long bettingOptionId;
}
