package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model;


import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name = "betting_channel_comment")
@ToString
public class BettingChannelComment extends BaseEntity {

    /**
     * 댓글 고유 id
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글을 달 배팅채널 id
     * */
    private Long bettingChannelId;

    /**
     * 댓글 작성자 정보
     * */
    @Embedded
    private WriterInfo writer;

    /**
     * 댓글 내용
     * */
    @Embedded
    private Content content;

    /**
     * 해당 댓글을 좋아요 한 수
     * */
    private int likeCnt;

    /**
     * 해당 댓글을 인용한 수
     * */
    private int replyCnt;

    protected BettingChannelComment() {}

    /**
     * 배팅채널 댓글 생성자
     * @param channelId 댓글을 달 배팅 id
     * @param content 댓글 정보
     * @param writer  작성자 정보
     */
    public BettingChannelComment(Long channelId, Content content, WriterInfo writer) {
        this.bettingChannelId = channelId;
        this.content = content;
        this.writer = writer;
        this.likeCnt = 0;
        this.replyCnt = 0;
    }

    public void setContent(Content content) {
        this.content = content;
    }


    public void increaseLikeCnt() {
        this.likeCnt++;
    }

    public void increaseReplyCnt() {
        this.replyCnt++;
    }
}
