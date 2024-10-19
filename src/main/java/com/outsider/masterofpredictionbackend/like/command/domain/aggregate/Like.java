package com.outsider.masterofpredictionbackend.like.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tbl_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type")
    private LikeType likeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_type")
    private ViewType viewType;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    public Like(LikeType likeType, ViewType viewType, Long userId, Long targetId) {
        this.likeType = likeType;
        this.viewType = viewType;
        this.userId = userId;
        this.targetId = targetId;
    }
}
