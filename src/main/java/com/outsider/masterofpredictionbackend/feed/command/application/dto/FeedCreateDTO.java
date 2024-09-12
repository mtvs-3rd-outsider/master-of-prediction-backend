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
import java.util.stream.Collectors;

@Data
public class FeedCreateDTO {
    private AuthorType authorType;
    private String title;
    private String content;
    private ChannelType channelType;
    private User user;
    private Guest guest;
    private List<String> mediaFileUrls;
    private List<String> youtubeUrls;

    public Feed toEntity() {
        Feed feed = new Feed(
                this.authorType,
                this.title,
                this.content,
                LocalDateTime.now(),
                null,
                this.channelType,
                0,
                0,
                0,
                0,
                this.user,
                this.guest,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );

        if (mediaFileUrls != null) {
            feed.setMediaFiles(mediaFileUrls.stream()
                    .map(url -> new MediaFile(url, feed))
                    .collect(Collectors.toList()));
        }

        if (youtubeUrls != null) {
            feed.setYoutubeVideos(youtubeUrls.stream()
                    .map(url -> new YouTubeVideo(url, feed))
                    .collect(Collectors.toList()));
        }

        return feed;
    }
}