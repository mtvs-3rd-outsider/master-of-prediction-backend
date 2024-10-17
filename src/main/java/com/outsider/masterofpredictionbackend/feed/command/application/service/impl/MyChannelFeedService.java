package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MyChannelFeedService  {
    private final FeedRepository feedRepository;
    private final DTOConverterFacade converterFacade;

    public MyChannelFeedService(FeedRepository feedRepository,DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
    }

    public Page<FeedsResponseDTO> getFeeds(ChannelType channelType, Long channelId, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findByChannel_ChannelTypeAndChannel_ChannelId(channelType, channelId, pageable);
        return feedPage.map(feed -> converterFacade.fromEntity(feed, FeedsResponseDTO.class));
    }
}