package com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString
public class BettingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bettingId;

    @Column(nullable = false)
    private BigDecimal point;

    @Column(nullable = false)
    private Long bettingOptionId;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private LocalTime orderTime;

    public BettingOrder(Long userId, Long bettingId, BigDecimal point, Long bettingOptionId, LocalDate orderDate, LocalTime orderTime) {
        this.userId = userId;
        this.bettingId = bettingId;
        this.point = point;
        this.bettingOptionId = bettingOptionId;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }
}
