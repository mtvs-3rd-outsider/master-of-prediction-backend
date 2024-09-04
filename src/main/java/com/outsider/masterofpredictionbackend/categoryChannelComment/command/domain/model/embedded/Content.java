package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;

/**
 * 카테고리 채널 게시물의 댓글 내용들
 */
@Embeddable
@Getter
@ToString
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

    protected Content() {}

    public Content(String content, String imageUrl, String password) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.password = password;
    }
}
