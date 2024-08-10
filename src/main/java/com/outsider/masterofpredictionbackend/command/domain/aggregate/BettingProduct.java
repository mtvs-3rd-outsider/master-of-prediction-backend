package com.outsider.masterofpredictionbackend.command.domain.aggregate;


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

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private long categoryCode;

    @Column
    private LocalDate deadlineDate;

    @Column
    private LocalTime deadlineTime;

    protected BettingProduct() {
    }

    public BettingProduct(String title, String content, long categoryCode, LocalDate deadlineDate, LocalTime deadlineTime) {
        this.title = title;
        this.content = content;
        this.categoryCode = categoryCode;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

}
