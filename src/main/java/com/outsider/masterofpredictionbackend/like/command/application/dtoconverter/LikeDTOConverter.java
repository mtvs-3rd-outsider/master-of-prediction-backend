package com.outsider.masterofpredictionbackend.like.command.application.dtoconverter;

import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.Like;

public class LikeDTOConverter {

    public static LikeDTO toDTO(Like like) {
        if (like == null) {
            return null;
        }

        LikeDTO dto = new LikeDTO();
        dto.setLikeType(like.getLikeType());
        dto.setViewType(like.getViewType());
        dto.setUserId(like.getUserId());
        dto.setTargetId(like.getTargetId());

        return dto;
    }

    public static Like toEntity(LikeDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Like(
                dto.getLikeType(),
                dto.getViewType(),
                dto.getUserId(),
                dto.getTargetId()
        );
    }


}