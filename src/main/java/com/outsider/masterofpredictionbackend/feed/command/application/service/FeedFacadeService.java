package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.*;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalReplyService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedFacadeService {
    private final FeedCreateService feedCreateService;
    private final FeedReadService feedReadService;
    private final FeedUpdateService feedUpdateService;
    private final FeedDeleteService feedDeleteService;
    private final ExternalUserService externalUserService;
    private final ExternalCommentService externalCommentService;
    private final ExternalReplyService externalReplyService;

    public FeedFacadeService(FeedCreateService feedCreateService,
                             FeedReadService feedReadService,
                             FeedUpdateService feedUpdateService,
                             FeedDeleteService feedDeleteService,
                             ExternalUserService externalUserService,
                             ExternalCommentService externalCommentService,
                             ExternalReplyService externalReplyService) {
        this.feedCreateService = feedCreateService;
        this.feedReadService = feedReadService;
        this.feedUpdateService = feedUpdateService;
        this.feedDeleteService = feedDeleteService;
        this.externalUserService = externalUserService;
        this.externalCommentService = externalCommentService;
        this.externalReplyService = externalReplyService;
    }

    // Feed 생성 메서드
    public void createFeed(FeedCreateDTO feedCreateDTO) {
        feedCreateService.createFeed(feedCreateDTO);
    }

    // Feed 조회 메서드
    public FeedResponseDTO getFeed(Long feedId) {
        return feedReadService.getFeed(feedId);
    }

    // Feed 업데이트 메서드
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO) {
        feedUpdateService.updateFeed(feedId, feedUpdateDTO);
    }

    // Feed 삭제 메서드
    public void deleteFeed(Long feedId) {
        feedDeleteService.deleteFeed(feedId);
    }

    // 사용자 정보 조회 메서드

    // 댓글 조회 메서드
    public List<CommentDTO> getComments(Long feedId) {
        return externalCommentService.getCommentsByFeedId(feedId);
    }

    // 답글 조회 메서드
    public List<ReplyDTO> getReplies(Long commentId) {
        return externalReplyService.getRepliesByCommentId(commentId);
    }

}