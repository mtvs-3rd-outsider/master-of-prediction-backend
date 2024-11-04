package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FeedReuploadService {
    private final FeedRepository feedRepository;
    private final FeedReadService feedReadService;

    public FeedReuploadService(FeedRepository feedRepository, FeedReadService feedReadService) {
        this.feedRepository = feedRepository;
        this.feedReadService = feedReadService;
    }

    @Transactional
    public void reuploadFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        // Add user to reupload list if not already present
        feed.addReupLoadUser(userId);
        feed.setShortAt(LocalDateTime.now());
        feed.setShareCount(feed.getShareCount()+1);
        feedRepository.save(feed);
    }

    @Transactional
    public void removeReupload(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        // Remove user from reupload list
        feed.removeReupLoadUser(userId);
        feed.setShortAt(feed.getCreatedAt());
        feed.setShareCount(feed.getShareCount()-1);
        feedRepository.save(feed);
    }
}
