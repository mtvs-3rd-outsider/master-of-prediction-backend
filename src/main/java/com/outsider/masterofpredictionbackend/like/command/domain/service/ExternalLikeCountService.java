package com.outsider.masterofpredictionbackend.like.command.domain.service;


import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;

public interface ExternalLikeCountService {

    void updateLikeCount(LikeCountIdDTO likeCountIdDto, boolean isLike);

    int getLikeCount(LikeCountIdDTO likeCountIdDto);
}
