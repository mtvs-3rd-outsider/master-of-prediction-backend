package com.outsider.masterofpredictionbackend.feed.command.domain.repository;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
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

    @NotNull
    Page<Feed> findAll(@NotNull Pageable pageable);

    @Modifying
    @Query("UPDATE Feed f SET f.viewCount = f.viewCount + 1 WHERE f.id = :feedId")
    void incrementViewCount(@Param("feedId") Long feedId);

    Page<Feed> findByChannel_ChannelTypeAndChannel_ChannelId(ChannelType channelType, Long channelId, Pageable pageable);
}

