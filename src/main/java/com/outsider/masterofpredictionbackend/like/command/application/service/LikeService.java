package com.outsider.masterofpredictionbackend.like.command.application.service;

import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.dtoconverter.LikeDTOConverter;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.Like;
import com.outsider.masterofpredictionbackend.like.command.domain.repository.LikeRepository;
import com.outsider.masterofpredictionbackend.like.command.domain.service.ExternalLikeCountService;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCountId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    public final LikeRepository likeRepository;
    public final ExternalLikeCountService externalLikeCountService;

    public LikeService(LikeRepository likeRepository, ExternalLikeCountService externalLikeCountService) {
        this.likeRepository = likeRepository;
        this.externalLikeCountService = externalLikeCountService;
    }
    @Transactional
    public boolean isLike(LikeDTO dto) {
        LikeCountIdDTO likeCountIdDto = new LikeCountIdDTO(dto.getTargetId(), dto.getLikeType());

        boolean exists = likeRepository.existsByUserIdAndLikeTypeAndViewTypeAndTargetId(
                dto.getUserId(), dto.getLikeType(), dto.getViewType(), dto.getTargetId()
        );

        if (exists) {
            // 좋아요가 이미 존재하면 삭제
            likeRepository.deleteByUserIdAndLikeTypeAndViewTypeAndTargetId(
                    dto.getUserId(), dto.getLikeType(), dto.getViewType(), dto.getTargetId()
            );
            externalLikeCountService.updateLikeCount(likeCountIdDto, false);
            return false;
        } else {
            // 좋아요가 존재하지 않으면 추가
            Like like = LikeDTOConverter.toEntity(dto);
            likeRepository.save(like);
            externalLikeCountService.updateLikeCount(likeCountIdDto, true);
            return true;
        }
    }


    //좋아요 갯수 가져오기
    public int getLikeCount(LikeDTO dto){
        LikeCountIdDTO likeCountIdDto = new LikeCountIdDTO(dto.getTargetId(),dto.getLikeType());
        return externalLikeCountService.getLikeCount(likeCountIdDto);
    }
}
