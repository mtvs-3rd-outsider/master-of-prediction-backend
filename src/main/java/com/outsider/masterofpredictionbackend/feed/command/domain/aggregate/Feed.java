package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.QuoteBetting;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tbl_feed")
@Getter
@Setter
@NoArgsConstructor
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "feed_author_type", nullable = false)
    private AuthorType authorType;

    @Column(name ="feed_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "feed_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "feed_short_at", nullable = false)
    private LocalDateTime shortAt;

    @Column(name = "feed_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "feed_view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "feed_likes_count", nullable = false)
    private int likesCount = 0;

    @Column(name = "feed_comments_count", nullable = false)
    private int commentsCount = 0;

    @Column(name = "feed_share_count",nullable = false)
    private int shareCount = 0;

    @Column(name = "feed_isquote")
    private Boolean isquote = false;

    @Embedded
    private QuoteFeed quoteFeed;

    @Embedded
    private User user;

    @Embedded
    private Guest guest;

    @Embedded
    private Channel channel;

    @Column(name = "feed_islike")
    private Boolean isLike;

    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<YouTubeVideo> youtubeVideos = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "feed_reupload_users",
            joinColumns = @JoinColumn(name = "feed_id"))
    @Column(name = "user_id")
    private List<Long> reupLoadUsers = new ArrayList<>();

//    @Column(name = "feed_is_quote_betting")
//    private Boolean isQuoteBetting = false;
//
//    @Embedded
//    private QuoteBetting quoteBetting;


    // reupLoadUsers 추가
    public void addReupLoadUser(Long userId) {
        if (!this.reupLoadUsers.contains(userId)) {
            this.reupLoadUsers.add(userId);
        }
    }

    // reupLoadUsers 제거
    public void removeReupLoadUser(Long userId) {
        this.reupLoadUsers.remove(userId);
    }

    // reupLoad 여부 확인
    public boolean isReupLoadedBy(Long userId) {
        return this.reupLoadUsers.contains(userId);
    }

    // 모든 reupLoadUsers 초기화
    public void clearReupLoadUsers() {
        this.reupLoadUsers.clear();
    }


    public Feed(AuthorType authorType, String content, LocalDateTime createdAt,LocalDateTime shortAt, LocalDateTime updatedAt, int viewCount, int likesCount, int commentsCount, int shareCount, User user, Guest guest, Channel channel, boolean isLike, List<MediaFile> mediaFiles, List<YouTubeVideo> youtubeVideos) {
        this.authorType = authorType;
        this.content = content;
        this.createdAt = createdAt;
        this.shortAt = shortAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.shareCount = shareCount;
        this.user = user;
        this.guest = guest;
        this.channel = channel;
        this.isLike = isLike;
        this.mediaFiles = mediaFiles;
        this.youtubeVideos = youtubeVideos;
//        this.isQuoteBetting = isQuoteBetting;
//        this.quoteBetting = quoteBetting;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", authorType=" + authorType +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", shortAt=" + shortAt +
                ", updatedAt=" + updatedAt +
                ", viewCount=" + viewCount +
                ", likesCount=" + likesCount +
                ", commentsCount=" + commentsCount +
                ", shareCount=" + shareCount +
                ", user=" + user +
                ", guest=" + guest +
                ", channel=" + channel +
                ", isLike=" + isLike +
                ", mediaFiles=" + mediaFiles +
                ", youtubeVideos=" + youtubeVideos +
                '}';
    }

}
