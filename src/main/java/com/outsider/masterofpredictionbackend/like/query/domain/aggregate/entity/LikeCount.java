package com.outsider.masterofpredictionbackend.like.query.domain.aggregate.entity;

import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

@Entity
@Table(name = "tbl_like_count")
@Getter
@Setter
@NoArgsConstructor
public class LikeCount {

    @EmbeddedId
    private LikeCountId id;

    @Column(name = "like_count")
    private int likeCount;

    public LikeCount(LikeCountId id, int likeCount) {
        this.id = id;
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "LikeCount{" +
                "id=" + id +
                ", likeCount=" + likeCount +
                '}';
    }
}