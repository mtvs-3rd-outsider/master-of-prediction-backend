package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.CommentDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FeedReadService {
    private final FeedRepository feedRepository;
    private final ExternalCommentService externalCommentService;
    private final FeedViewCountService feedViewCountService;
    private final ExternalUserService externalUserService;

    @Autowired
    public FeedReadService(FeedRepository feedRepository,
                           ExternalCommentService externalCommentService,
                           FeedViewCountService feedViewCountService,ExternalUserService externalUserService) {
        this.feedRepository = feedRepository;
        this.externalCommentService = externalCommentService;
        this.feedViewCountService = feedViewCountService;
        this.externalUserService = externalUserService;
    }

    @Transactional(readOnly = false)
    public FeedResponseDTO getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        FeedResponseDTO feedResponseDTO = fromFeed(feed);

        // 댓글과 답글을 한 번에 가져옵니다.
        Map<Long, List<CommentDTO>> commentsWithReplies = externalCommentService.getCommentsWithRepliesByFeedId(feedId);
        feedResponseDTO.setCommentsWithReplies(commentsWithReplies);

        // 비동기적으로 조회수를 증가시킵니다.
        feedViewCountService.incrementViewCount(feedId);

        return feedResponseDTO;
    }

    public FeedResponseDTO fromFeed(Feed feed) {
        FeedResponseDTO feedResponseDTO = new FeedResponseDTO();
        feedResponseDTO.setId(feed.getId());
        feedResponseDTO.setAuthorType(feed.getAuthorType());
        feedResponseDTO.setTitle(feed.getTitle());
        feedResponseDTO.setContent(feed.getContent());
        feedResponseDTO.setCreatedAt(feed.getCreatedAt());
        feedResponseDTO.setUpdatedAt(feed.getUpdatedAt());
        feedResponseDTO.setViewCount(feed.getViewCount());
        feedResponseDTO.setChannelType(feed.getChannelType());
        feedResponseDTO.setUser(feed.getUser().getUserId() != null ? externalUserService.getUser(feed.getUser().getUserId()) : null);
        feedResponseDTO.setGuest(feed.getGuest() != null ? feed.getGuest() : null);
        feedResponseDTO.setMediaFiles(feed.getMediaFiles());
        feedResponseDTO.setYouTubeVideos(feed.getYoutubeVideos());
        feedResponseDTO.setLikesCount(feed.getLikesCount());
        feedResponseDTO.setCommentsCount(feed.getCommentsCount());
        feedResponseDTO.setQuoteCount(feed.getQuoteCount());
        return feedResponseDTO;
    }
}