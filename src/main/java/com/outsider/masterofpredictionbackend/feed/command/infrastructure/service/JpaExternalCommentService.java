package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.CommentDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JpaExternalCommentService implements ExternalCommentService {
    @Override
    public List<CommentDTO> getCommentsByFeedId(Long feedId) {
        return List.of();
    }

    @Override
    public Map<Long, List<CommentDTO>> getCommentsWithRepliesByFeedId(Long feedId) {
        return Map.of();
    }
}
