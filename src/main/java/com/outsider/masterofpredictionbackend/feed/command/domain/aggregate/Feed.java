package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_feed")
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private long id;

    @Column(name = "feed_author_type", nullable = false)
    private String authorType;

    @Column(name = "feed_title", nullable = false)
    private String title;

    @Column(name ="feed_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "feed_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "feed_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "feed_view_count", nullable = false)
    private int viewCount;

    @Column(name = "feed_channel_type", nullable = false)
    private String channelType; // mychannel 또는 categorychannel

    @Embedded
    private User user;

    @Embedded
    private Guest guest;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageFile> imageFiles;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YouTubeVideo> youtubeVideos;

    public Feed(String authorType, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, String channelType, User user, Guest guest, List<ImageFile> imageFiles, List<YouTubeVideo> youtubeVideos) {
        this.authorType = authorType;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.channelType = channelType;
        this.user = user;
        this.guest = guest;
        this.imageFiles = imageFiles;
        this.youtubeVideos = youtubeVideos;
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
                ", user=" + user +
                ", guest=" + guest +
                ", imageFiles=" + imageFiles +
                ", youtubeVideos=" + youtubeVideos +
                '}';
    }
}
