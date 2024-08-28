package com.outsider.masterofpredictionbackend.feed.command.domain.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.ReplyDTO;

import java.util.List;
import java.util.Map;

public interface ExternalReplyService {
    List<ReplyDTO> getRepliesByCommentId(Long commentId);

    Map<Long, List<ReplyDTO>> getRepliesByCommentIds(List<Long> commentIds);
}