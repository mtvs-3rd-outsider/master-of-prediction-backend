package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedsResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HotTopicChannelFeedService {

    private final FeedRepository feedRepository;
    private final FeedsResponseDTOConverter converterFacade;
    private final ExternalLikeService externalLikeService;

    @Autowired
    public HotTopicChannelFeedService(FeedRepository feedRepository, FeedsResponseDTOConverter converterFacade, ExternalLikeService externalLikeService) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
        this.externalLikeService = externalLikeService;
    }

    public Page<FeedsResponseDTO> getFeeds(Pageable pageable,long userId) {
        Page<Feed> feedPage = feedRepository.findAll(pageable);
        return feedPage.map(feed -> {
            int likeCount = externalLikeService.getLikeCount(new LikeDTO(LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getUser().getUserId(), feed.getId()));
            boolean isLiked = externalLikeService.checkUserLike(userId, LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getId());

            feed.setLikesCount(likeCount);
            feed.setIsLike(isLiked);

            return converterFacade.fromEntity(feed);
        });
    }
}