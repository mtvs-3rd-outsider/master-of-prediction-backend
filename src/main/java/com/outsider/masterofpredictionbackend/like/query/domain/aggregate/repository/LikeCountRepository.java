package com.outsider.masterofpredictionbackend.like.query.domain.aggregate.repository;

import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCount;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCountId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCountRepository extends JpaRepository<LikeCount, LikeCountId> {

}
