package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HotTopicFeedResponseDTO {
    private long id;
    private AuthorType authorType;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCount;
    private ChannelType channelType;
    private UserDTO user;
    private GuestDTO guest;
    private List<String> mediaFileUrls;
    private List<String> youtubeUrls;
    private int likesCount;
    private int commentsCount;
    private int quoteCount;


    @Data
    public static class GuestDTO {
        private String guestId;
    }


}