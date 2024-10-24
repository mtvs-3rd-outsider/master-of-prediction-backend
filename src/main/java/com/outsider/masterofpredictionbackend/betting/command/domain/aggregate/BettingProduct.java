package com.outsider.masterofpredictionbackend.betting.command.domain.aggregate;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "betting_product")
@ToString
@Getter
public class BettingProduct  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column
    private Long userId;

    @Column(nullable = false)
    private long categoryCode;

    @Column(nullable = false)
    private LocalDate deadlineDate;

    @Column(nullable = false)
    private LocalTime deadlineTime;

    @Column(nullable = false)
    private boolean isBlind;

    @Column(nullable = true)
    private String blindName;

    @Setter
    @Column(nullable = false)
    private BettingProductState state = BettingProductState.PROGRESS;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Setter
    @Column(nullable = true)
    private Long winningOption;

    @Setter
    @Column(nullable = true)
    private BettingProductType type = BettingProductType.USER;

    @Setter
    @Column(nullable = true)
    private String apiGameId;

    protected BettingProduct() {
    }

    public BettingProduct(String title, String content, long userId ,long categoryCode,
                          LocalDate deadlineDate, LocalTime deadlineTime, boolean isBlind, String blindName) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.categoryCode = categoryCode;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.isBlind = isBlind;
        this.blindName = blindName;
    }

}
