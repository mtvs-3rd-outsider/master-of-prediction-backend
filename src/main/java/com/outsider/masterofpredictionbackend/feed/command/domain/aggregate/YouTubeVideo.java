package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_youtube_video")
public class YouTubeVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private long id;

    @Column(name = "youtube_url", nullable = false)
    private String youtubeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    public YouTubeVideo(String youtubeUrl, Feed feed) {
        this.youtubeUrl = youtubeUrl;
        this.feed = feed;
    }

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "id=" + id +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", feed=" + feed +
                '}';
    }
}
