package com.outsider.masterofpredictionbackend.betting.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "Betting")
public class BettingAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String Title;

    @Column
    private String content;

    private long categoryCode;

    @Column
    @Lob
    private String mainImgUrl;

    @Column
    @Lob
    private String item;
}
