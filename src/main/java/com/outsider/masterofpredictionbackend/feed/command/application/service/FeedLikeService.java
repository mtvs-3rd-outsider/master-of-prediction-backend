package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Like;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.LikeRepository;
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

        Optional<Like> existingLike = likeRepository.findByFeedIdAndUserId(feedId, userId);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.delete(existingLike.get());
            feed.decrementLikesCount();
        } else {
            // 좋아요를 누르지 않았다면 좋아요 추가
            Like newLike = new Like(feed, userId);
            likeRepository.save(newLike);
            feed.incrementLikesCount();
            isLike=true;
        }

        feedRepository.save(feed);
        return isLike;
    }
}
