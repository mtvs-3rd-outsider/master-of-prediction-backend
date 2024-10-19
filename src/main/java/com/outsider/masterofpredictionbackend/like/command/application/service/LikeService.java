package com.outsider.masterofpredictionbackend.like.command.application.service;

import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.dtoconverter.LikeDTOConverter;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.Like;
import com.outsider.masterofpredictionbackend.like.command.domain.repository.LikeRepository;
import com.outsider.masterofpredictionbackend.like.command.domain.service.ExternalLikeCountService;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCountId;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    public final LikeRepository likeRepository;
    public final ExternalLikeCountService externalLikeCountService;

    public LikeService(LikeRepository likeRepository, ExternalLikeCountService externalLikeCountService) {
        this.likeRepository = likeRepository;
        this.externalLikeCountService = externalLikeCountService;
    }

    public boolean isLike(LikeDTO dto){
        Like like = LikeDTOConverter.toEntity(dto);
        LikeCountIdDTO likeCountIdDto = new LikeCountIdDTO(dto.getTargetId(),dto.getLikeType());
        if(likeRepository.existsByUserIdAndViewTypeAndLikeTypeAndTargetId(dto.getUserId(),dto.getViewType(),dto.getLikeType(),dto.getTargetId())){
            likeRepository.delete(like);
            externalLikeCountService.updateLikeCount(likeCountIdDto,false);
            return false;
        }else{
            likeRepository.save(like);
            externalLikeCountService.updateLikeCount(likeCountIdDto,true);
            return true;
        }
    }


    //좋아요 갯수 가져오기
    public int getLikeCount(LikeDTO dto){
        LikeCountIdDTO likeCountIdDto = new LikeCountIdDTO(dto.getTargetId(),dto.getLikeType());
        return externalLikeCountService.getLikeCount(likeCountIdDto);
    }
}
