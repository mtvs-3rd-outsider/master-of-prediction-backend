package com.outsider.masterofpredictionbackend.categoryChannelComment.query.dto;

import lombok.Data;

@Data
public class CategoryChannelCommentViewDTO {

    private Long commentId;
    private String writer;
    private String writeAt;
    private String comment;
    private String imageUrl;
}
