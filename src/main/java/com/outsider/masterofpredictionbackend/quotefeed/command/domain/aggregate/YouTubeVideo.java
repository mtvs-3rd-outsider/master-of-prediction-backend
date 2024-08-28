package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "quotepeed_youtube_video")
@Getter
@NoArgsConstructor
@Table(name = "tbl_quotepeed_youtube_video")
public class YouTubeVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private long id;

    @Column(name = "youtube_url", nullable = false)
    private String youtubeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotefeed_id", nullable = false)
    private QuoteFeed quoteFeed;

    public YouTubeVideo(String youtubeUrl, QuoteFeed quoteFeed) {
        this.youtubeUrl = youtubeUrl;
        this.quoteFeed = quoteFeed;
    }

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "id=" + id +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", quoteFeed=" + quoteFeed +
                '}';
    }
}
