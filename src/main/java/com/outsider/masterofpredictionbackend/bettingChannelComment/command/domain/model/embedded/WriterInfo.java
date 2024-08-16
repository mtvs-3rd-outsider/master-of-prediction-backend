package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * 베팅 채널 게시글에 댓글을 작성하는 사용자 정보를 나타내는 필드
 */
@Embeddable
@Getter
public class WriterInfo {

    /**
     * 댓글 작성자 id(DB에서 관리하는 고유 id값)
     * */
    private Long writerNo;

    /**
     * 댓글 작성자 이름 혹은 닉네임
     * */
    private String writerName;

    protected WriterInfo() {}

    /**
     * 배팅댓글 사용자 정보
     *
     * @param writerNo  댓글 작성자 번호
     * @param writerName 작성자 이름
     */
    public WriterInfo(Long writerNo, String writerName) {
        this.writerNo = writerNo;
        this.writerName = writerName;
    }
}
