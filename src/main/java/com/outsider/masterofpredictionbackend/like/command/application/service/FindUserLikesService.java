package com.outsider.masterofpredictionbackend.like.command.application.service;

import com.outsider.masterofpredictionbackend.like.command.application.dto.UserLikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.Like;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.like.command.domain.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FindUserLikesService {

    private final LikeRepository likeRepository;

    @Autowired
    public FindUserLikesService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    //유저가 해당 feed에 좋아요를 눌렀는지
    public boolean checkUserLike(Long userId, LikeType likeType, ViewType viewType, Long targetId) {
        return likeRepository.existsByUserIdAndLikeTypeAndViewTypeAndTargetId(userId, likeType, viewType, targetId);
    }
}