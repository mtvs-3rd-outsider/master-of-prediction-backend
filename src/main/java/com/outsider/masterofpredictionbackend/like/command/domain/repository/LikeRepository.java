package com.outsider.masterofpredictionbackend.like.command.domain.repository;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.Like;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserIdAndViewTypeAndLikeTypeAndTargetId(Long userId, ViewType viewType, LikeType likeType, Long targetId);

    boolean existsByUserIdAndLikeTypeAndViewTypeAndTargetId(Long userId, LikeType likeType, ViewType viewType, Long targetId);


    void deleteByUserIdAndLikeTypeAndViewTypeAndTargetId(
            Long userId, LikeType likeType, ViewType viewType, Long targetId
    );
}