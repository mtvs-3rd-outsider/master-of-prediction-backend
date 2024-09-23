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
    private final FileService fileService;

    public FeedFacadeService(FeedCreateService feedCreateService,
                             FeedReadService feedReadService,
                             FeedUpdateService feedUpdateService,
                             FeedDeleteService feedDeleteService,
                             ExternalCommentService externalCommentService,
                             ExternalReplyService externalReplyService,
                             FileService fileService) {
        this.feedCreateService = feedCreateService;
        this.feedReadService = feedReadService;
        this.feedUpdateService = feedUpdateService;
        this.feedDeleteService = feedDeleteService;
        this.externalCommentService = externalCommentService;
        this.externalReplyService = externalReplyService;
        this.fileService = fileService;
    }

    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO, List<MultipartFile> files, List<String> youtubeUrls) throws Exception {
        List<String> fileUrls = uploadFiles(files);
        feedCreateDTO.setMediaFileUrls(fileUrls);
        feedCreateDTO.setYoutubeUrls(youtubeUrls);
        return feedCreateService.createFeed(feedCreateDTO);
    }

    // Feed 조회 메서드
    public FeedResponseDTO getFeed(Long feedId) {
        return feedReadService.getFeed(feedId);
    }

    // Feed 업데이트 메서드
    @Transactional
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO, List<MultipartFile> files, List<String> youtubeUrls) throws Exception {
        FeedResponseDTO existingFeed = getFeed(feedId);
        validateUpdatePermission(existingFeed, feedUpdateDTO);

        feedUpdateService.updateFeed(feedId, feedUpdateDTO, files, youtubeUrls);
    }

    private List<String> uploadFiles(List<MultipartFile> files) throws Exception {
        List<String> fileUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileUrl = fileService.uploadFile(file);
                fileUrls.add(fileUrl);
            }
        }
        return fileUrls;
    }

    private void validateUpdatePermission(FeedResponseDTO existingFeed, FeedUpdateDTO feedUpdateDTO) {
        if(existingFeed.getAuthorType() == AuthorType.GUEST) {
            if(!(existingFeed.getGuest().getGuestId().equals(feedUpdateDTO.getGuest().getGuestId()))
                    || !(existingFeed.getGuest().getGuestPassword().equals(feedUpdateDTO.getGuest().getGuestPassword()))) {
                throw new AccessDeniedException("아이디 혹은 비밀번호가 틀립니다.");
            }
        }
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