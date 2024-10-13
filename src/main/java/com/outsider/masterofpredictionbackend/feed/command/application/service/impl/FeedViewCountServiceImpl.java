package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedViewCountService;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedViewCountServiceImpl implements FeedViewCountService {
    private final FeedRepository feedRepository;

    @Autowired
    public FeedViewCountServiceImpl(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Async
    @Transactional
    public void incrementViewCount(Long feedId) {
        feedRepository.incrementViewCount(feedId);
    }
}