package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedDeleteService {
    private final FeedRepository feedRepository;

    @Autowired
    public FeedDeleteService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    // Feed 삭제 메서드
    public void deleteFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));
        feedRepository.delete(feed);
    }
}