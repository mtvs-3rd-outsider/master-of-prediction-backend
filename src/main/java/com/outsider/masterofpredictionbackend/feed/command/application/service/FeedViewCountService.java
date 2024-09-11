package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedViewCountService {
    private final FeedRepository feedRepository;

    @Autowired
    public FeedViewCountService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Async
    @Transactional
    public void incrementViewCount(Long feedId) {
        feedRepository.incrementViewCount(feedId);
    }
}