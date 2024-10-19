package com.outsider.masterofpredictionbackend.like.query.application.dto;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import lombok.Data;


@Data
public class LikeCountIdDTO {
    private long targetId;

    private LikeType likeType;

    public LikeCountIdDTO() {
    }

    public LikeCountIdDTO(Long targetId, LikeType likeType) {
        this.targetId = targetId;
        this.likeType = likeType;
    }
}
