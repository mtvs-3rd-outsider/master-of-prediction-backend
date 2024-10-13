package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.CommentDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.application.service.impl.FeedViewCountServiceImpl;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
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
    private final DTOConverterFacade converterFacade;

    @Autowired
    public FeedReadService(FeedRepository feedRepository,
                           ExternalCommentService externalCommentService,
                           FeedViewCountService feedViewCountService, ExternalUserService externalUserService,
                           DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.externalCommentService = externalCommentService;
        this.feedViewCountService = feedViewCountService;
        this.converterFacade = converterFacade;
    }

    @Transactional(readOnly = false)
    public FeedResponseDTO getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        FeedResponseDTO feedResponseDTO = converterFacade.toEntity(feed);

        // 댓글과 답글을 한 번에 가져옵니다.
        Map<Long, List<CommentDTO>> commentsWithReplies = externalCommentService.getCommentsWithRepliesByFeedId(feedId);
        feedResponseDTO.setCommentsWithReplies(commentsWithReplies);

        // 비동기적으로 조회수를 증가시킵니다.
        feedViewCountService.incrementViewCount(feedId);

        return feedResponseDTO;
    }


}