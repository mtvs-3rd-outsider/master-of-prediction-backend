package com.outsider.masterofpredictionbackend.feed.command.domain.repository;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>{
    @NotNull
    Optional<Feed> findById(@NotNull Long id);

    @Query("SELECT f FROM Feed f WHERE f.id < :lastId ORDER BY f.viewCount DESC")
    List<Feed> findHotTopicFeedsAfter(@Param("lastId") Long lastId, Pageable pageable);

    @Query("SELECT f FROM Feed f ORDER BY f.viewCount DESC")
    List<Feed> findInitialHotTopicFeeds(Pageable pageable);

    @Modifying
    @Query("UPDATE Feed f SET f.viewCount = f.viewCount + 1 WHERE f.id = :feedId")
    void incrementViewCount(@Param("feedId") Long feedId);
}
