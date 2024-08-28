package com.outsider.masterofpredictionbackend.feed.command.domain.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.CommentDTO;

import java.util.List;

public interface ExternalCommentService {
    List<CommentDTO> getCommentsByFeedId(Long feedId);
}
