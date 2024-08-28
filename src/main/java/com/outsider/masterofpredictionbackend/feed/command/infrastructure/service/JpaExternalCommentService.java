package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.CommentDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaExternalCommentService implements ExternalCommentService {
    @Override
    public List<CommentDTO> getCommentsByFeedId(Long feedId) {
        return List.of();
    }
}
