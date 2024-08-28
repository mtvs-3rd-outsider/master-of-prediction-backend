package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.ReplyDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalReplyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JpaExternalReplyService implements ExternalReplyService {
    @Override
    public List<ReplyDTO> getRepliesByCommentId(Long commentId) {
        return List.of();
    }

    @Override
    public Map<Long, List<ReplyDTO>> getRepliesByCommentIds(List<Long> commentIds) {
        return Map.of();
    }
}
