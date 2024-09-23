package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.VideoFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.ImageFileRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.VideoFileRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.YouTubeVideoRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedCreateService {
    private final FeedRepository feedRepository;
    private final ExternalUserService externalUserService;
    @Autowired
    public FeedCreateService(FeedRepository feedRepository, ExternalUserService externalUserService) {
        this.feedRepository = feedRepository;
        this.externalUserService = externalUserService;

    }

    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO) {
        Feed feed = feedCreateDTO.toEntity();
        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }

    private boolean isImageFile(String url) {
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.endsWith(".jpg") || lowercaseUrl.endsWith(".jpeg")
                || lowercaseUrl.endsWith(".png") || lowercaseUrl.endsWith(".gif");
    }

    private boolean isVideoFile(String url) {
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.endsWith(".mp4") || lowercaseUrl.endsWith(".avi")
                || lowercaseUrl.endsWith(".mov");
    }
}