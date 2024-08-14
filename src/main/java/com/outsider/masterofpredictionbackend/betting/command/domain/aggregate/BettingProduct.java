package com.outsider.masterofpredictionbackend.betting.command.domain.aggregate;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "betting_product")
@ToString
@Getter
public class BettingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column
    private long userId;

    @Column(nullable = false)
    private long categoryCode;

    @Column(nullable = false)
    private LocalDate deadlineDate;

    @Column(nullable = false)
    private LocalTime deadlineTime;

    @Column(nullable = false)
    private boolean isBlind;

    protected BettingProduct() {
    }

    public BettingProduct(String title, String content, long userId ,long categoryCode, LocalDate deadlineDate, LocalTime deadlineTime, boolean isBlind) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.categoryCode = categoryCode;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.isBlind = isBlind;
    }

}