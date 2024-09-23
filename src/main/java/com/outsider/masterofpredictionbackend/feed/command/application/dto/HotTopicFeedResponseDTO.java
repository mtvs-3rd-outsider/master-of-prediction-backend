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
    public static class UserDTO {
        private String userId;
        private String userName;
        private String userImg;
    }

    @Data
    public static class GuestDTO {
        private String guestId;
    }

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

        if (feed.getUser() != null) {
            UserDTO userDto = new UserDTO();
            userDto.setUserId(feed.getUser().getUserId());
            userDto.setUserName(feed.getUser().getUserId());  // Assuming userName is the same as userId
            // Set userImg if available
            dto.setUser(userDto);
        }

        if (feed.getGuest() != null) {
            GuestDTO guestDto = new GuestDTO();
            guestDto.setGuestId(feed.getGuest().getGuestId());
            dto.setGuest(guestDto);
        }

        dto.setMediaFileUrls(feed.getMediaFiles().stream()
                .map(MediaFile::getFileUrl)
                .collect(Collectors.toList()));
        dto.setYoutubeUrls(feed.getYoutubeVideos().stream()
                .map(YouTubeVideo::getYoutubeUrl)
                .collect(Collectors.toList()));
        dto.setLikesCount(feed.getLikesCount());
        dto.setCommentsCount(feed.getCommentsCount());
        dto.setQuoteCount(feed.getQuoteCount());
        return dto;
    }
}