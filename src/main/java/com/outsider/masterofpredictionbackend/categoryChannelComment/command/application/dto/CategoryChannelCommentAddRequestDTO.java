package com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryChannelCommentAddRequestDTO {
    private Long channelId;

    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String comment;

    private String password;

    private String imageUrl;// 이미지 혹은 이모티콘 url
}
