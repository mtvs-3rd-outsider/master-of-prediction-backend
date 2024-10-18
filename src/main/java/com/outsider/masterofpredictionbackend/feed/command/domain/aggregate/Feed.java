package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "feed_title", nullable = false)
    private String title;

    @Column(name ="feed_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "feed_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "feed_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "feed_view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "feed_likes_count", nullable = false)
    private int likesCount = 0;

    @Column(name = "feed_comments_count", nullable = false)
    private int commentsCount = 0;

    @Column(name = "feed_quote_count",nullable = false)
    private int quoteCount = 0;

    @Embedded
    private User user;

    @Embedded
    private Guest guest;

    @Embedded
    private Channel channel;

    @Column(name = "feed_like")
    private Boolean isLike;

    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<YouTubeVideo> youtubeVideos = new ArrayList<>();

    public Feed(AuthorType authorType, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, int likesCount, int commentsCount, int quoteCount, User user, Guest guest, Channel channel, boolean isLike, List<MediaFile> mediaFiles, List<YouTubeVideo> youtubeVideos) {
        this.authorType = authorType;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.quoteCount = quoteCount;
        this.user = user;
        this.guest = guest;
        this.channel = channel;
        this.isLike = isLike;
        this.mediaFiles = mediaFiles;
        this.youtubeVideos = youtubeVideos;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", authorType=" + authorType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", viewCount=" + viewCount +
                ", likesCount=" + likesCount +
                ", commentsCount=" + commentsCount +
                ", quoteCount=" + quoteCount +
                ", user=" + user +
                ", guest=" + guest +
                ", channel=" + channel +
                ", isLike=" + isLike +
                ", mediaFiles=" + mediaFiles +
                ", youtubeVideos=" + youtubeVideos +
                '}';
    }
}
