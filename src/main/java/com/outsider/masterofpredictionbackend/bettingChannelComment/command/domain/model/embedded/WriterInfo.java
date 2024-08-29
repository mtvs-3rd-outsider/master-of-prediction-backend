package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;

/**
 * 베팅 채널 게시글에 댓글을 작성하는 사용자 정보를 나타내는 필드
 */
@Embeddable
@Getter
@ToString
public class WriterInfo {

    /**
     * 댓글 작성자 id(DB에서 관리하는 고유 id값)
     * */
    private Long writerNo;


    protected WriterInfo() {}

    /**
     * 배팅댓글 사용자 정보
     *
     * @param writerNo  댓글 작성자 고유 번호
     */
    public WriterInfo(Long writerNo) {
        this.writerNo = writerNo;
    }
}
