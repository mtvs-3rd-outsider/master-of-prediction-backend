package com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingOrderDTO {

    private Long userId;

    @NotNull
    private Long bettingId;

    @NotNull
    private BigDecimal point;

    @NotNull
    private Long bettingOptionId;

    private LocalDate orderDate;

    private LocalTime orderTime;

    public BettingOrderDTO(Long userId, Long bettingId, BigDecimal point, Long bettingOptionId) {
        this.userId = userId;
        this.bettingId = bettingId;
        this.point = point;
        this.bettingOptionId = bettingOptionId;
    }

    public BettingOrderDTO(Long bettingId, BigDecimal point, Long bettingOptionId, LocalDate orderDate, LocalTime orderTime) {
        this.bettingId = bettingId;
        this.point = point;
        this.bettingOptionId = bettingOptionId;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public static BettingOrderDTO entityConvertToDTO(BettingOrder bettingOrder) {
        return new BettingOrderDTO(
                bettingOrder.getBettingId(),
                bettingOrder.getPoint(),
                bettingOrder.getBettingOptionId(),
                bettingOrder.getOrderDate(),
                bettingOrder.getOrderTime()
        );
    }

}
