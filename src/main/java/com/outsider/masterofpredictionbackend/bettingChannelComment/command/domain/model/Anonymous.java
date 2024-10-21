package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Anonymous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long channelId;

    private Long userId;

    private Long anonymousNo;// 익명댓글1 등.. 에서 번호
}
