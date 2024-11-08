package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuoteFeedDTO {
    private Long quoteId;
    private String quoteContent;
    private LocalDateTime quoteCreateAt;
    private List<String> mediaFileUrls;
    private List<String> youtubeUrls;
    private UserDTO quoteUser;
    private GuestDTO quoteGuest;

    public QuoteFeedDTO(Long quoteId, String quoteContent, LocalDateTime quoteCreateAt, List<String> mediaFileUrls, List<String> youtubeUrls, UserDTO quoteUser, GuestDTO quoteGuest) {
        this.quoteId = quoteId;
        this.quoteContent = quoteContent;
        this.quoteCreateAt = quoteCreateAt;
        this.mediaFileUrls = mediaFileUrls;
        this.youtubeUrls = youtubeUrls;
        this.quoteUser = quoteUser;
        this.quoteGuest = quoteGuest;
    }
}