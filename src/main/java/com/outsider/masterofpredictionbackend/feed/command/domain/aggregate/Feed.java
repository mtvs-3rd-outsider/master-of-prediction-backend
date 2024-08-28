package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column(name = "feed_channel_type", nullable = false)
    private ChannelType channelType; // mychannel 또는 categorychannel

    @Column(name = "feed_view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "feed_likes_count", nullable = false)
    private int likesCount = 0;

    @Column(name = "feed_comments_count", nullable = false)
    private int commentsCount = 0;

    @Embedded
    private User user;

    @Embedded
    private Guest guest;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> like;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageFile> imageFiles;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YouTubeVideo> youtubeVideos;

    public Feed(AuthorType authorType, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, ChannelType channelType, int viewCount, int likesCount, int commentsCount, User user, Guest guest, List<Like> like, List<ImageFile> imageFiles, List<YouTubeVideo> youtubeVideos) {
        this.authorType = authorType;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.channelType = channelType;
        this.viewCount = viewCount;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.user = user;
        this.guest = guest;
        this.like = like;
        this.imageFiles = imageFiles;
        this.youtubeVideos = youtubeVideos;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void decrementLikesCount() {
        this.likesCount--;
    }

    public void incrementLikesCount() {
        this.likesCount++;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", authorType='" + authorType + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", viewCount=" + viewCount +
                ", channelType='" + channelType + '\'' +
                ", likesCount=" + likesCount +
                ", commentsCount=" + commentsCount +
                ", user=" + user +
                ", guest=" + guest +
                ", imageFiles=" + imageFiles +
                ", youtubeVideos=" + youtubeVideos +
                '}';
    }
}
