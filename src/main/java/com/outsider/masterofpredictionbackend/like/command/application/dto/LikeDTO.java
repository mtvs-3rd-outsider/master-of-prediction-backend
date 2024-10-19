package com.outsider.masterofpredictionbackend.like.command.application.dto;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import lombok.Data;

@Data
public class LikeDTO {
    private LikeType likeType;
    private ViewType viewType;
    private Long userId;
    private Long targetId;

    public LikeDTO() {
    }

    public LikeDTO(LikeType likeType, ViewType viewType, Long userId, Long targetId) {
        this.likeType = likeType;
        this.viewType = viewType;
        this.userId = userId;
        this.targetId = targetId;
    }
}
