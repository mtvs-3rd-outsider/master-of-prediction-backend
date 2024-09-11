package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private User user;
    private Guest guest;
    private List<ImageFile> imageFiles;
    private List<YouTubeVideo> youTubeVideos;
    private int likesCount;
    private int commentsCount;
    private int quoteCount;

    public static HotTopicFeedResponseDTO fromFeed(Feed feed) {
        HotTopicFeedResponseDTO dto = new HotTopicFeedResponseDTO();
        dto.setId(feed.getId());
        dto.setAuthorType(feed.getAuthorType());
        dto.setTitle(feed.getTitle());
        dto.setContent(feed.getContent());
        dto.setCreatedAt(feed.getCreatedAt());
        dto.setUpdatedAt(feed.getUpdatedAt());
        dto.setViewCount(feed.getViewCount());
        dto.setChannelType(feed.getChannelType());
        dto.setUser(feed.getUser());
        dto.setGuest(feed.getGuest());
        dto.setImageFiles(feed.getImageFiles());
        dto.setYouTubeVideos(feed.getYoutubeVideos());
        dto.setLikesCount(feed.getLikesCount());
        dto.setCommentsCount(feed.getCommentsCount());
        dto.setQuoteCount(feed.getQuoteCount());
        return dto;
    }
}