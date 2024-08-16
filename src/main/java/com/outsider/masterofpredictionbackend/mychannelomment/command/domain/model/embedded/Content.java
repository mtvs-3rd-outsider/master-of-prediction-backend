package com.outsider.masterofpredictionbackend.mychannelomment.command.domain.model.embedded;

import jakarta.persistence.Embeddable;

/**
 * 내 채널 게시물의 댓글 내용들
 */
@Embeddable
public class Content {

    /**
     * 댓글 내용
     * */
    private String content;

    /**
     * 이미지 링크
     * */
    private String imageUrl;
}
