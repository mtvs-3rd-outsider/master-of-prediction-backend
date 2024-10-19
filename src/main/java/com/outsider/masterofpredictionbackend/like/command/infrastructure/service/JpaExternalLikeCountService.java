package com.outsider.masterofpredictionbackend.like.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.like.command.domain.service.ExternalLikeCountService;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.like.query.application.service.LikeCountService;
import org.springframework.stereotype.Service;

@Service
public class JpaExternalLikeCountService implements ExternalLikeCountService {

    public final LikeCountService likeCountService;

    public JpaExternalLikeCountService(LikeCountService likeCountService) {
        this.likeCountService = likeCountService;
    }

    @Override
    public void updateLikeCount(LikeCountIdDTO likeCountIdDto, boolean isLike) {
        likeCountService.updateLikeCount(likeCountIdDto, isLike);
    }

    @Override
    public int getLikeCount(LikeCountIdDTO likeCountIdDto) {
        return likeCountService.getLikeCount(likeCountIdDto);
    }


}
