package com.outsider.masterofpredictionbackend.like.command.application.dto;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeDTO {
    private LikeType likeType;
    private Long targetId;
    private boolean isLiked;
}