package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedsResponseDTO {
    private long id;
    private AuthorType authorType;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCount;
    private UserDTO user;
    private GuestDTO guest;
    private List<String> mediaFileUrls;
    private List<String> youtubeUrls;
    private int likesCount;
    private int commentsCount;
    private int quoteCount;
}