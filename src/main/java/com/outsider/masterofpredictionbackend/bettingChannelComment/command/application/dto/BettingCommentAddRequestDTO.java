package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BettingCommentAddRequestDTO {
    private Long userNo;

    private Long bettingId;

    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String comment;

    private String imageUrl;// 이미지 혹은 이모티콘 url
}
