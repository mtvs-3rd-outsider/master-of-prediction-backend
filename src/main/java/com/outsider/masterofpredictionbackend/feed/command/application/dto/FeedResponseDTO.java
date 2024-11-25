package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class FeedResponseDTO {
    private long id;
    private AuthorType authorType;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime shortAt;  // shortAt 추가
    private LocalDateTime updatedAt;
    private int viewCount;
    private UserDTO user;
    private GuestDTO guest;
    private List<MediaFile> mediaFiles;
    private List<YouTubeVideo> youTubeVideos;
    private List<CommentDTO> commentDTOS;
    private List<ReplyDTO> replyDTOS;
    private Boolean isLike;
    private int likesCount;
    private int commentsCount;
    private int shareCount;
    private Boolean isShare;
    private Boolean isQuote;  // 인용 여부 추가
    private QuoteFeedDTO quoteFeed;  // 인용된 피드 정보
    private ChannelDTO channel;
    public FeedResponseDTO(long id, AuthorType authorType, String content,
                           LocalDateTime createdAt, LocalDateTime shortAt, LocalDateTime updatedAt,
                           int viewCount, UserDTO user, GuestDTO guest,
                           List<MediaFile> mediaFiles, List<YouTubeVideo> youTubeVideos,
                           List<CommentDTO> commentDTOS, List<ReplyDTO> replyDTOS,
                           Boolean isLike, int likesCount, int commentsCount,
                           int shareCount, Boolean isShare, Boolean isQuote, QuoteFeedDTO quoteFeed) {
        this.id = id;
        this.authorType = authorType;
        this.content = content;
        this.createdAt = createdAt;
        this.shortAt = shortAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.user = user;
        this.guest = guest;
        this.mediaFiles = mediaFiles;
        this.youTubeVideos = youTubeVideos;
        this.commentDTOS = commentDTOS;
        this.replyDTOS = replyDTOS;
        this.isLike = isLike;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.shareCount = shareCount;
        this.isShare = isShare;
        this.isQuote = isQuote;
        this.quoteFeed = quoteFeed;
    }
}