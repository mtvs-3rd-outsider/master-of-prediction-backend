package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDTO {
    private Long id;
    private String content;
    private Long userId;
    private Long commentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}