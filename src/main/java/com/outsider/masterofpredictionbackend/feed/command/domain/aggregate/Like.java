package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity(name = "feed_like")
@Table(name = "tbl_feed_like")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Like(Feed feed,Long userId) {
        this.feed = feed;
        this.userId=userId;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 좋아요 생성을 위한 정적 팩토리 메서드
    public static Like createLike(Feed feed, Long userId) {
        return Like.builder()
                .feed(feed)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", feed=" + feed +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}