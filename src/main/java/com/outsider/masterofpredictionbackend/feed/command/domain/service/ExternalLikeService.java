package com.outsider.masterofpredictionbackend.feed.command.domain.service;

import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.dto.UserLikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;

import java.util.List;

public interface ExternalLikeService {
    int getLikeCount(LikeDTO dto);
    UserLikeDTO checkUserLike(Long userId, LikeType likeType, ViewType viewType, Long targetId);
    void saveLikeCount(LikeCountIdDTO likeCountIdDTO);
}
