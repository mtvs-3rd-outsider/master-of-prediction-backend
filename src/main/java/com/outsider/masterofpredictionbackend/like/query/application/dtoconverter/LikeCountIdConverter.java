package com.outsider.masterofpredictionbackend.like.query.application.dtoconverter;

import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity.LikeCountId;
import org.springframework.stereotype.Component;

@Component
public class LikeCountIdConverter {

    public LikeCountId toEntity(LikeCountIdDTO dto) {
        if (dto == null) {
            return null;
        }
        return new LikeCountId(dto.getTargetId(), dto.getLikeType());
    }

    public LikeCountIdDTO toDTO(LikeCountId entity) {
        if (entity == null) {
            return null;
        }
        LikeCountIdDTO dto = new LikeCountIdDTO();
        dto.setTargetId(entity.getTargetId());
        dto.setLikeType(entity.getLikeType());
        return dto;
    }
}