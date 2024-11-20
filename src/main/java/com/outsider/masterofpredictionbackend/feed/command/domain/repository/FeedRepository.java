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

    Page<Feed> findAllByOrderByShortAtDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE Feed f SET f.viewCount = f.viewCount + 1 WHERE f.id = :feedId")
    void incrementViewCount(@Param("feedId") Long feedId);

    @Query("SELECT f FROM Feed f WHERE f.channel.channelType = :channelType AND f.channel.channelId = :channelId ORDER BY f.shortAt DESC")
    Page<Feed> findByChannel_ChannelTypeAndChannel_ChannelId(ChannelType channelType, Long channelId, Pageable pageable);

    @Query("SELECT f FROM Feed f WHERE " +
            "(f.channel.channelType = :channelType AND f.channel.channelId = :channelId) OR " +
            ":channelId MEMBER OF f.reupLoadUsers " +
            "ORDER BY f.shortAt DESC")
    Page<Feed> findByChannel_ChannelTypeAndChannel_ChannelIdOrReuploadedBy(
            ChannelType channelType,
            Long channelId,
            Pageable pageable
    );

    @Query("SELECT f FROM Feed f WHERE f.channel.channelType = :channelType AND f.channel.channelId IN :channelIds ORDER BY f.shortAt DESC")
    List<Feed> findByChannelTypeAndChannelIds(
            @Param("channelType") ChannelType channelType,
            @Param("channelIds") List<Long> channelIds,
            Pageable pageable
    );

    @Query("SELECT DISTINCT f FROM Feed f WHERE " +
            "(f.channel.channelType = :channelType AND f.channel.channelId IN :channelIds) OR " +
            "EXISTS (SELECT 1 FROM f.reupLoadUsers ru WHERE ru IN :channelIds) " +
            "ORDER BY f.shortAt DESC")
    Page<Feed> findByChannel_ChannelTypeAndChannel_ChannelIdsOrReuploadedBy(
            @Param("channelType") ChannelType channelType,
            @Param("channelIds") List<Long> channelIds,
            Pageable pageable
    );
    @Query("SELECT f FROM Feed f WHERE f.id < 0")
    @NotNull
    Page<Feed> findAllByIdLessThanZero(@NotNull Pageable pageable);

    List<Feed> findAllByIdIn(List<Long> ids);
}

