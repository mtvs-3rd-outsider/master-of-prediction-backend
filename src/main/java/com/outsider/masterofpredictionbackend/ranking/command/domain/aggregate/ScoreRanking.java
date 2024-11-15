package com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class ScoreRanking {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private int score;

    @Column(name = "user_rank", nullable = false)
    private int rank;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // 생성자
    public ScoreRanking(Long userId) {
        this.userId = userId;
        this.score = 0;
        this.rank = Integer.MAX_VALUE;
        this.lastUpdated = LocalDateTime.now();
    }

    public ScoreRanking() {}

    // 게터 및 세터
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
