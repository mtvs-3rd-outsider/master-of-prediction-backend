package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "댓글 추가 DTO")
@Data
public class BettingCommentAddRequestDTO {
    @Schema(description = "사용자 고유 id(식별자)")
    private Long userNo;

    @Schema(description = "배팅 id")
    private Long bettingId;

    @Schema(description = "댓글 내용")
    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String comment;

    @Schema(description = "이미지 url")
    private String imageUrl;// 이미지 혹은 이모티콘 url
    
    @Schema(description = "익명댓글 여부")
    private boolean isAnonymous;
}
