package com.outsider.masterofpredictionbackend.like.command.domain.repository;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.Like;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.userId = :userId AND l.viewType = :viewType AND l.likeType = :likeType AND l.targetId IN :targetIds")
    List<Like> findByUserIdAndViewTypeAndLikeTypeAndTargetIdIn(
            @Param("userId") String userId,
            @Param("viewType") ViewType viewType,
            @Param("likeType") LikeType likeType,
            @Param("targetIds") List<Long> targetIds
    );

    boolean existsByUserIdAndViewTypeAndLikeTypeAndTargetId(String userId, ViewType viewType, LikeType likeType, Long targetId);
}