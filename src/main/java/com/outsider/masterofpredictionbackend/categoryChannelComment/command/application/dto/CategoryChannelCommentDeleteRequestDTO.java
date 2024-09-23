package com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryChannelCommentDeleteRequestDTO {
    @NotBlank(message = "댓글 id는 필수 입력 사항 입니다.")
    private Long commentId;
    private String password;
}
