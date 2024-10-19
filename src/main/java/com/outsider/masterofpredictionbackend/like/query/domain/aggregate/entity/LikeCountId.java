package com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class LikeCountId implements Serializable {

    @Column(name = "target_id")
    private long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type")
    private LikeType likeType;

    public LikeCountId(long targetId, LikeType likeType) {
        this.targetId = targetId;
        this.likeType = likeType;
    }

}