package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.GuestDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.QuoteUser;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteFeed {
    @Column(name = "quote_feed_id")
    private Long quoteId;

    @Column(name = "quote_feed_content", columnDefinition = "TEXT")
    private String quoteContent;

    @Column(name = "quote_feed_created_at")
    private LocalDateTime quoteCreateAt;

    // 사용자 ID만 저장
    @Column(name = "quote_user_id")
    private Long quoteUserId;

    @ElementCollection
    @CollectionTable(
            name = "quote_feed_media_files",
            joinColumns = @JoinColumn(name = "feed_id")
    )
    @Column(name = "media_file_url")
    private List<String> mediaFileUrls = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "quote_feed_youtube_videos",
            joinColumns = @JoinColumn(name = "feed_id")
    )
    @Column(name = "youtube_url")
    private List<String> youtubeUrls = new ArrayList<>();

}