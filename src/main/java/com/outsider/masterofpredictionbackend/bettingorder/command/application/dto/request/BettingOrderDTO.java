package com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BettingOrderDTO {

    private Long userId;

    @NotNull
    private Long bettingId;

    @NotNull
    private BigDecimal point;

    @NotNull
    private Long bettingOptionId;

    @NotNull
    private LocalDate orderDate;

    @NotNull
    private LocalTime orderTime;

}
