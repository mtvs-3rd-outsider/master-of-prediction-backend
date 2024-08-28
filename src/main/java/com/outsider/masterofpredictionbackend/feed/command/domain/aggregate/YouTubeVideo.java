package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "youtube_video")
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

    public YouTubeVideo(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
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
