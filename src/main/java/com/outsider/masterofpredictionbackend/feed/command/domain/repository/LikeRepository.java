package com.outsider.masterofpredictionbackend.feed.command.domain.repository;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {
    Optional<Like> findByFeedIdAndUserId(Long feedId, Long userId);
}
