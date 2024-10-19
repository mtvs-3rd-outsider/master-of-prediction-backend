package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.dto.UserLikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.service.FindUserLikesService;
import com.outsider.masterofpredictionbackend.like.command.application.service.LikeService;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.like.query.application.service.LikeCountService;
import org.springframework.stereotype.Service;

@Service
public class JpaExternalLikeService implements ExternalLikeService {

    public final LikeService likeService;
    public final FindUserLikesService findUserLikesService;
    public final LikeCountService likeCountService;

    public JpaExternalLikeService(LikeService likeService, FindUserLikesService findUserLikesService, LikeCountService likeCountService) {
        this.likeService = likeService;
        this.findUserLikesService = findUserLikesService;
        this.likeCountService = likeCountService;
    }
    @Override
    public int getLikeCount(LikeDTO dto) {
        return likeService.getLikeCount(dto);
    }

    @Override
    public UserLikeDTO checkUserLike(Long userId, LikeType likeType, ViewType viewType, Long targetId) {
        return findUserLikesService.checkUserLike(userId,likeType,viewType,targetId);
    }

    @Override
    public void saveLikeCount(LikeCountIdDTO likeCountIdDTO){
        likeCountService.saveLikeCount(likeCountIdDTO);
    }

}
