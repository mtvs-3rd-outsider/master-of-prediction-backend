package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import co.elastic.clients.transport.endpoints.BooleanEndpoint;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class FeedsResponseDTO {
    private long id;
    private AuthorType authorType;
    private String title;  // title 추가
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime shortAt;
    private LocalDateTime updatedAt;
    private int viewCount;
    private UserDTO user;
    private GuestDTO guest;
    private Boolean isLike;
    private List<String> mediaFileUrls;
    private List<String> youtubeUrls;
    private int likesCount;
    private int commentsCount;
    private int shareCount;
    private Boolean isShare;
    private Boolean isQuote;  // 인용 여부 추가
    private QuoteFeedDTO quoteFeed;  // 인용된 피드 정보
}