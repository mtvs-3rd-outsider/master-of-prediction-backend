package com.outsider.masterofpredictionbackend.like.query.application.service;

import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.like.query.application.dtoconverter.LikeCountIdConverter;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCount;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCountId;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.repository.LikeCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeCountService {

    private final LikeCountRepository likeCountRepository;
    private final LikeCountIdConverter likeCountIdConverter;

    @Autowired
    public LikeCountService(LikeCountRepository likeCountRepository, LikeCountIdConverter likeCountIdConverter) {
        this.likeCountRepository = likeCountRepository;
        this.likeCountIdConverter = likeCountIdConverter;
    }

    @Transactional
    public void updateLikeCount(LikeCountIdDTO likeCountIdDto, boolean isLike) {
        LikeCountId likeCountId = likeCountIdConverter.toEntity(likeCountIdDto);
        LikeCount likeCount = likeCountRepository.findById(likeCountId)
                .orElse(new LikeCount(likeCountId, 0));
        if (isLike) {
            likeCount.setLikeCount(likeCount.getLikeCount() + 1);
        }else{
            likeCount.setLikeCount(likeCount.getLikeCount() - 1);
        }
    }

    @Transactional(readOnly = true)
    public int getLikeCount(LikeCountIdDTO likeCountIdDto) {
        LikeCountId likeCountId = likeCountIdConverter.toEntity(likeCountIdDto);
        return likeCountRepository.findById(likeCountId)
                .map(LikeCount::getLikeCount)
                .orElse(0); // 좋아요 수가 없으면 0을 반환
    }

    public void saveLikeCount(LikeCountIdDTO likeCountIdDto) {
        LikeCountId likeCountId = likeCountIdConverter.toEntity(likeCountIdDto);
        LikeCount likeCount = new LikeCount(likeCountId, 0);
        likeCountRepository.save(likeCount);
    }
}