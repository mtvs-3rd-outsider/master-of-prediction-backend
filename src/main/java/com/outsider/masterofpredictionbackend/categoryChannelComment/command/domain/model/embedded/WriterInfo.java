package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.model.embedded;

import jakarta.persistence.Embeddable;

/**
 * 카테고리 채널 게시글에 댓글을 작성하는 사용자 정보를 나타내는 필드
 */
@Embeddable
public class WriterInfo {

    /**
     * 댓글 작성자 id(DB에서 관리하는 고유 id값)
     * */
    private Long writerId;

    /**
     * 댓글 작성자 이름 혹은 닉네임
     * */
    private String writerName;

    /**
     * 로그인 한 사용자인지 나타내는 필드
     * */
    private Boolean isLoginUser;
}
