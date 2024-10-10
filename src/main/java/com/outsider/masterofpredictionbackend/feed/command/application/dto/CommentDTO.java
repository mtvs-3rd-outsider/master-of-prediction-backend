package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setReplies(List<CommentDTO> replies) {
    }
}