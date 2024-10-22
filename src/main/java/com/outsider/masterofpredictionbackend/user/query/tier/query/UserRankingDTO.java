package com.outsider.masterofpredictionbackend.user.query.tier.query;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserRankingDTO {

    private Long userId;
    private BigDecimal points;
    private int rank;
    private LocalDateTime lastUpdated;
    private String userName; // userName 추가
    private String displayName;
    private String userImg;

    public UserRankingDTO(Long userId, BigDecimal points, int rank, LocalDateTime lastUpdated, String userName) {
        this.userId = userId;
        this.points = points;
        this.rank = rank;
        this.lastUpdated = lastUpdated;
        this.userName = userName;
    }

    // 기본 생성자
    public UserRankingDTO() {
    }

}
