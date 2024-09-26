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

    @Cacheable(value = "userLikes", key = "#userId + '_' + #viewType + '_' + #targetId")
    public boolean hasUserLiked(String userId, ViewType viewType, LikeType likeType, Long targetId) {
        return likeRepository.existsByUserIdAndViewTypeAndLikeTypeAndTargetId(userId, viewType, likeType, targetId);
    }

    public List<UserLikeDTO> batchCheckUserLikes(String userId, ViewType viewType, List<Long> targetIds) {
        List<UserLikeDTO> result = new ArrayList<>();

        for (LikeType likeType : LikeType.values()) {
            List<Like> likes = likeRepository.findByUserIdAndViewTypeAndLikeTypeAndTargetIdIn(userId, viewType, likeType, targetIds);

            Map<Long, Boolean> likeStatusMap = targetIds.stream()
                    .collect(Collectors.toMap(
                            id -> id,
                            id -> likes.stream().anyMatch(like -> like.getTargetId().equals(id))
                    ));

            likeStatusMap.forEach((targetId, isLiked) ->
                    result.add(new UserLikeDTO(likeType, targetId, isLiked))
            );
        }

        return result;
    }
}