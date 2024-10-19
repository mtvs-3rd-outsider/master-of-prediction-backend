package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.FeedConstants;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.dto.UserLikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.like.command.domain.service.ExternalLikeCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class HotTopicChannelFeedService {

    private final FeedRepository feedRepository;
    private final DTOConverterFacade converterFacade;
    private final ExternalLikeService externalLikeService;
    @Autowired
    public HotTopicChannelFeedService(FeedRepository feedRepository, DTOConverterFacade converterFacade, ExternalLikeService externalLikeService) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
        this.externalLikeService = externalLikeService;
    }


    public Page<FeedsResponseDTO> getFeeds(Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findAll(pageable);
        feedPage.forEach(feed -> feed.setLikesCount(externalLikeService.getLikeCount(new LikeDTO(LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getUser().getUserId(), feed.getId()))));
        feedPage.map(feed ->externalLikeService.checkUserLike(feed.getUser().getUserId(),LikeType.FEED,ViewType.HOTTOPICCHANNEL,feed.getId()));
        return feedPage.map(feed ->converterFacade.fromEntity(feed,FeedsResponseDTO.class));
    }
}