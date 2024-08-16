package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCommentRequestDTO {
    @NotBlank(message = "프로필 사진은 비어있을 수 없습니다.")
    private String profileLink;

    @NotBlank(message = "사용자 이름은 비어있을 수 없습니다.")
    private String username;

    @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    private String nickname;

    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String comment;

    private String imageUrl;// 이미지 혹은 이모티콘 url
}
