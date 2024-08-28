package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedUpdateDTO {
    private AuthorType authorType;
    private String title;
    private String content;
    private LocalDate updateAt;
    private ChannelType channelType;
    private User user;
    private Guest guest;
    private List<ImageFile> imageFiles;
    private List<YouTubeVideo> youTubeVideos;

    public Feed updateFeed(Feed feed) {
        feed.setAuthorType(this.getAuthorType());
        feed.setTitle(this.getTitle());
        feed.setContent(this.getContent());
        feed.setUpdatedAt(LocalDateTime.now()); // 현재 시간으로 업데이트
        feed.setChannelType(this.getChannelType());
        feed.setUser(this.getUser());
        feed.setGuest(this.getGuest());

        return feed;
    }
}