package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded.Comment;
import com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded.Reply;
import com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded.FeedType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_quotefeed")
public class QuoteFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotefeed_id")
    private long id;

    @Column(name = "quotefeed_author_type", nullable = false)
    private String authorType;

    @Column(name = "quotefeed_title", nullable = false)
    private String title;

    @Column(name ="quotefeed_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "quotefeed_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "quotefeed_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "quotefeed_view_count", nullable = false)
    private int viewCount;

    @Column(name = "quotefeed_channel_type", nullable = false)
    private String channelType; // mychannel 또는 categorychannel

    @Embedded
    private User user;

    @Embedded
    private Guest guest;

    @Embedded
    private Comment comment;

    @Embedded
    private Reply reply;

    @Embedded
    private FeedType feedType;

    @OneToMany(mappedBy = "quoteFeed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageFile> imageFiles;

    @OneToMany(mappedBy = "quoteFeed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YouTubeVideo> youtubeVideos;

    public QuoteFeed(String authorType, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, String channelType, User user, Guest guest, Comment comment, Reply reply, FeedType feedType, List<ImageFile> imageFiles, List<YouTubeVideo> youtubeVideos) {
        this.authorType = authorType;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.channelType = channelType;
        this.user = user;
        this.guest = guest;
        this.comment = comment;
        this.reply = reply;
        this.feedType = feedType;
        this.imageFiles = imageFiles;
        this.youtubeVideos = youtubeVideos;
    }

    @Override
    public String toString() {
        return "QuoteFeed{" +
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
                ", comment=" + comment +
                ", reply=" + reply +
                ", feedType=" + feedType +
                ", imageFiles=" + imageFiles +
                ", youtubeVideos=" + youtubeVideos +
                '}';
    }
}
