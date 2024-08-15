package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * 베팅 채널 게시물의 댓글 내용들
 */
@Embeddable
@Getter
public class Content {

    /**
     * 댓글 내용
     * */
    private String content;

    /**
     * 이미지 링크
     * */
    private String imageUrl;

    protected Content() {}


    /**
     * 배팅댓글 내용
     *
     * @param content  댓글 내용
     * @param imageUrl 첨부 이미지 혹은 이모티콘 url
     */
    public Content(String content, String imageUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
