package com.outsider.masterofpredictionbackend.feed.command.application.service;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.*;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.stream.Collectors;

@Service
public class FeedCreateService {
    private final FeedRepository feedRepository;

    @Autowired
    public FeedCreateService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO) {
        Feed feed = feedToEntity(feedCreateDTO);
        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }


    public Feed feedToEntity(FeedCreateDTO dto) {
        Feed feed = new Feed(
                dto.getAuthorType(),
                dto.getTitle(),
                dto.getContent(),
                LocalDateTime.now(),
                null,
                dto.getChannelType(),
                0,
                0,
                0,
                0,
                dto.getUser(),
                dto.getGuest(),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );

        if (dto.getMediaFileUrls() != null) {
            feed.setMediaFiles(dto.getMediaFileUrls().stream()
                    .map(url -> new MediaFile(url, feed))
                    .collect(Collectors.toList()));
        }

        if (dto.getYoutubeUrls() != null) {
            feed.setYoutubeVideos(dto.getYoutubeUrls().stream()
                    .map(url -> new YouTubeVideo(url, feed))
                    .collect(Collectors.toList()));
        }

        return feed;
    }
}