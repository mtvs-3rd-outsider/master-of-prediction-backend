package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.HotTopicFeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotTopicFeedService {

    private final FeedRepository feedRepository;

    @Autowired
    public HotTopicFeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public List<HotTopicFeedResponseDTO> getInitialHotTopicFeeds(int size) {
        List<Feed> feeds = feedRepository.findInitialHotTopicFeeds(PageRequest.of(0, size));
        return feeds.stream()
                .map(HotTopicFeedResponseDTO::fromFeed)
                .collect(Collectors.toList());
    }

    public List<HotTopicFeedResponseDTO> getNextHotTopicFeeds(Long lastId, int size) {
        List<Feed> feeds = feedRepository.findHotTopicFeedsAfter(lastId, PageRequest.of(0, size));
        return feeds.stream()
                .map(HotTopicFeedResponseDTO::fromFeed)
                .collect(Collectors.toList());
    }
}