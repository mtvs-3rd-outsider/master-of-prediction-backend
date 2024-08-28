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
public class FeedResponseDTO {
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
    private List<CommentDTO> commentDTOS;
    private List<ReplyDTO> replyDTOS;
    private int likesCount;
    private int commentsCount;

    public static FeedResponseDTO fromFeed(Feed feed) {
        FeedResponseDTO feedResponseDTO = new FeedResponseDTO();
        feedResponseDTO.setId(feed.getId());
        feedResponseDTO.setAuthorType(feed.getAuthorType());
        feedResponseDTO.setTitle(feed.getTitle());
        feedResponseDTO.setContent(feed.getContent());
        feedResponseDTO.setCreatedAt(feed.getCreatedAt());
        feedResponseDTO.setUpdatedAt(feed.getUpdatedAt());
        feedResponseDTO.setViewCount(feed.getViewCount());
        feedResponseDTO.setChannelType(feed.getChannelType());
        feedResponseDTO.setUser(feed.getUser() != null ? feed.getUser() : null);
        feedResponseDTO.setGuest(feed.getGuest() != null ? feed.getGuest() : null);
        feedResponseDTO.setImageFiles(feed.getImageFiles());
        feedResponseDTO.setYouTubeVideos(feed.getYoutubeVideos());
        feedResponseDTO.setLikesCount(feed.getLikesCount());
        feedResponseDTO.setCommentsCount(feed.getCommentsCount());
        return feedResponseDTO;
    }
}