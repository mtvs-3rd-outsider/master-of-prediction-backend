package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model;


import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.WriterInfo;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "betting_channel_comment")
public class BettingChannelComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Embedded
    private WriterInfo writer;

    @Embedded
    private Content content;

    private int commentCnt;

    private int likeCnt;

    private int replyCnt;

    protected BettingChannelComment() {}

    /**
     * 배팅채널 댓글 생성자
     *
     * @param content 댓글 정보
     * @param writer  작성자 정보
     */
    public BettingChannelComment(Content content, WriterInfo writer) {
        this.content = content;
        this.writer = writer;
    }
}
