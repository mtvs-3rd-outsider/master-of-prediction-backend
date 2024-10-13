package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedsReadService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.FeedConstants;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class HotTopicChannelFeedService implements FeedsReadService<HotTopicParams> {

    private final FeedRepository feedRepository;
    private final DTOConverterFacade converterFacade;

    @Autowired
    public HotTopicChannelFeedService(FeedRepository feedRepository, DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
    }

    @Override
    public List<FeedsResponseDTO> getInitialFeeds(HotTopicParams params, Integer size) {
        int pageSize = (size != null) ? size : FeedConstants.DEFAULT_PAGE_SIZE.getValue();
        List<Feed> feeds = feedRepository.findInitialHotTopicFeeds(PageRequest.of(0, pageSize));
        return feeds.stream()
                .map(feed->converterFacade.fromEntity(feed,FeedsResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<FeedsResponseDTO> getNextFeeds(HotTopicParams params, Long lastId, Integer size) {
        int pageSize = (size != null) ? size : FeedConstants.DEFAULT_PAGE_SIZE.getValue();
        List<Feed> feeds = feedRepository.findHotTopicFeedsAfter(lastId, PageRequest.of(0, pageSize));
        return feeds.stream()
                .map(feed->converterFacade.fromEntity(feed,FeedsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChannelType getSupportedChannelType() {
        return ChannelType.HOT_TOPIC;
    }
}