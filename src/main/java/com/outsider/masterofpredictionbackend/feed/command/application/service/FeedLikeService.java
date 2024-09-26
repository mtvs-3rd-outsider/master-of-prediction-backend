package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.like.command.domain.repository.LikeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeedLikeService {
    private final LikeRepository likeRepository;
    private final FeedRepository feedRepository;

    @Autowired
    public FeedLikeService(LikeRepository likeRepository, FeedRepository feedRepository) {
        this.likeRepository = likeRepository;
        this.feedRepository = feedRepository;
    }
    public boolean toggleLike(Long feedId, String userId) {
        boolean isLike=false;
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));


        feedRepository.save(feed);
        return isLike;
    }
}
