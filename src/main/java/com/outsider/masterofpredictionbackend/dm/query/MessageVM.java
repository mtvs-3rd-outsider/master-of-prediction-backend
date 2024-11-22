package com.outsider.masterofpredictionbackend.dm.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class MessageVM {
    private String content;
    private UserVM user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Seoul")
    private Instant sent;
    private String roomId;
    private Integer id;
    private String replyToMessageId;
    private String replyContent; // 답글 내용 추가
    private String contentType;
    private String mediaUrl; // 미디어 URL 추가
    private List<ReactionVM> reactions; // 반응 리스트 추가
    public MessageVM(String content, UserVM user, Instant sent, String roomId, Integer id,
                     String replyToMessageId, String replyContent, String contentType,
                     String mediaUrl, List<ReactionVM> reactions) {
        this.content = content;
        this.user = user;
        this.sent = sent;
        this.roomId = roomId;
        this.id = id;
        this.replyToMessageId = replyToMessageId;
        this.replyContent = replyContent;
        this.contentType = contentType;
        this.mediaUrl = mediaUrl;
        this.reactions = reactions;
    }
}
