package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.util.UserId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedReadService {
    private final FeedRepository feedRepository;
    private final FeedViewCountService feedViewCountService;
    private final FeedResponseDTOConverter converterFacade;
    private final ExternalLikeService externalLikeService;

    @Autowired
    public FeedReadService(FeedRepository feedRepository,
                           ExternalLikeService externalLikeService,
                           FeedViewCountService feedViewCountService, ExternalUserService externalUserService,
                           FeedResponseDTOConverter converterFacade) {
        this.feedRepository = feedRepository;
        this.externalLikeService = externalLikeService;
        this.feedViewCountService = feedViewCountService;
        this.converterFacade = converterFacade;
    }

    @Transactional(readOnly = false)
    public FeedResponseDTO getFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));
        feed.setIsLike(externalLikeService.checkUserLike(userId, LikeType.FEED, ViewType.HOTTOPICCHANNEL,feedId));
        feed.setLikesCount(externalLikeService.getLikeCount(new LikeDTO(LikeType.FEED,ViewType.HOTTOPICCHANNEL,userId,feedId)));
        FeedResponseDTO feedResponseDTO = converterFacade.fromEntity(feed);

        // 비동기적으로 조회수를 증가시킵니다.
        feedViewCountService.incrementViewCount(feedId);

        return feedResponseDTO;
    }


}