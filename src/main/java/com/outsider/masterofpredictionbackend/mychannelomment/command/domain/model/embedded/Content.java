package com.outsider.masterofpredictionbackend.mychannelomment.command.domain.model.embedded;

import jakarta.persistence.Column;
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

    /**
     * 비회원이 댓글을 작성한 경우 댓글 수정/삭제를 위한 비밀번호
     * */
    @Column(name = "password", nullable = true)
    private String password;
}
