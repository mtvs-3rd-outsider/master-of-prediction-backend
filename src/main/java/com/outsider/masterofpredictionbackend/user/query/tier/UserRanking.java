package com.outsider.masterofpredictionbackend.user.query.tier;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_rankings", indexes = {
        @Index(name = "rank_index", columnList = "rank")
})
public class UserRanking {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private int rank;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;


    // 기본 생성자
    public UserRanking() {
        this.points = 0;
        this.rank = Integer.MAX_VALUE; // 초기 순위 설정
        this.lastUpdated = LocalDateTime.now();
    }

    // 매개변수 있는 생성자
    public UserRanking(Long userId) {
        this.userId = userId;
        this.points = 0;
        this.rank = Integer.MAX_VALUE; // 초기 순위 설정
        this.lastUpdated = LocalDateTime.now();
    }

    // Getter 및 Setter 메소드
    public Long getUserId() {
        return userId;
    }

    public int getPoints() {
        return points;
    }

    public int getRank() {
        return rank;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }



    public void setPoints(int points) {
        this.points = points;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;

    // equals, hashCode 메소드 구현 필요
}

    }
