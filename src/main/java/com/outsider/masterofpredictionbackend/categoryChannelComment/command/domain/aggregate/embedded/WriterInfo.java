package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * 카테고리 채널 게시글에 댓글을 작성하는 사용자 정보를 나타내는 필드
 */
@Embeddable
@Getter
public class WriterInfo {

    /**
     * 댓글 작성자 id(비회원의 경우 null)
     * */
    private Long writerNo;

    /**
     * 댓글 작성자 이름 혹은 닉네임
     * */
    private String writerName;

    /**
     * 로그인 한 사용자인지 나타내는 필드
     * */
    private Boolean isLoginUser;

    protected WriterInfo() {}

    public WriterInfo(Long writerNo, String writerName, Boolean isLoginUser) {
        this.writerNo = writerNo;
        this.writerName = writerName;
        this.isLoginUser = isLoginUser;
    }
}
