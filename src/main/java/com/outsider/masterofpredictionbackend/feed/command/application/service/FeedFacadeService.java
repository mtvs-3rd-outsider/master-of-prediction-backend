package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.*;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalReplyService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedFacadeService {
    private final FeedCreateService feedCreateService;
    private final FeedReadService feedReadService;
    private final FeedUpdateService feedUpdateService;
    private final FeedDeleteService feedDeleteService;
    private final ExternalCommentService externalCommentService;
    private final ExternalReplyService externalReplyService;

    public FeedFacadeService(FeedCreateService feedCreateService,
                             FeedReadService feedReadService,
                             FeedUpdateService feedUpdateService,
                             FeedDeleteService feedDeleteService,
                             ExternalCommentService externalCommentService,
                             ExternalReplyService externalReplyService) {
        this.feedCreateService = feedCreateService;
        this.feedReadService = feedReadService;
        this.feedUpdateService = feedUpdateService;
        this.feedDeleteService = feedDeleteService;
        this.externalCommentService = externalCommentService;
        this.externalReplyService = externalReplyService;
    }

    // Feed 생성 메서드
    public Long createFeed(FeedCreateDTO feedCreateDTO, List<MultipartFile> files, List<String> youtubeUrls) throws Exception {
        return feedCreateService.createFeed(feedCreateDTO,files,youtubeUrls);
    }

    // Feed 조회 메서드
    public FeedResponseDTO getFeed(Long feedId,Long userId) {
        return feedReadService.getFeed(feedId,userId);
    }

    // Feed 업데이트 메서드
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO, List<MultipartFile> files, List<String> youtubeUrls,Long userId) throws Exception {
        feedUpdateService.updateFeed(feedId, feedUpdateDTO, files, youtubeUrls,userId);
    }


    // Feed 삭제 메서드
    public void deleteFeed(Long feedId) {
        feedDeleteService.deleteFeed(feedId);
    }

    // 댓글 조회 메서드
    public List<CommentDTO> getComments(Long feedId) {
        return externalCommentService.getCommentsByFeedId(feedId);
    }

    // 답글 조회 메서드
    public List<ReplyDTO> getReplies(Long commentId) {
        return externalReplyService.getRepliesByCommentId(commentId);
    }
}