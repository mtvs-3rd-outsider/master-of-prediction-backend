package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.CommentDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.ReplyDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;

import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalReplyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedReadService {
    private final FeedRepository feedRepository;
    private final ExternalCommentService externalCommentService;
    private final ExternalReplyService externalReplyService;

    @Autowired
    public FeedReadService(FeedRepository feedRepository, ExternalCommentService externalCommentService, ExternalReplyService externalReplyService) {
        this.feedRepository = feedRepository;
        this.externalCommentService = externalCommentService;
        this.externalReplyService = externalReplyService;
    }

    // Feed 조회 메서드
    public FeedResponseDTO getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        // 조회수 증가
        feed.incrementViewCount();
        feedRepository.save(feed);

        FeedResponseDTO feedResponseDTO = FeedResponseDTO.fromFeed(feed);


        // 외부 도메인으로부터 feedResponseDTO에 댓글 추가
        List<CommentDTO> comments = externalCommentService.getCommentsByFeedId(feedId);
        feedResponseDTO.setCommentDTOS(comments);

        // 모든 댓글 ID 추출
        List<Long> commentIds = comments.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());

        // 모든 댓글에 대한 답글 가져오기
        Map<Long, List<ReplyDTO>> repliesMap = externalReplyService.getRepliesByCommentIds(commentIds);

        // 각 댓글에 해당하는 답글 설정
        comments.forEach(comment -> {
            List<ReplyDTO> replies = repliesMap.getOrDefault(comment.getId(), List.of());
            comment.setReplies(replies);
        });

        return feedResponseDTO;
    }
}