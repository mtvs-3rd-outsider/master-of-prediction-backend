package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BettingCommentUpdateRequestDTO {

    @NotBlank
    private Long commentId;

    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String updatedComment;

    private String updatedImageUrl;// 이미지 혹은 이모티콘 url
}