package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedsReadService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.MyChannelParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyChannelFeedService implements FeedsReadService<MyChannelParams> {
    private final FeedRepository feedRepository;
    private final DTOConverterFacade converterFacade;

    public MyChannelFeedService(FeedRepository feedRepository,DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
    }

    @Override
    public List<FeedsResponseDTO> getInitialFeeds(MyChannelParams params, Integer size) {
        return feedRepository.findMyChannelFeeds(params.getMyChannelId(), PageRequest.of(0, size)).stream()
                .map(feed->converterFacade.fromEntity(feed,FeedsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedsResponseDTO> getNextFeeds(MyChannelParams params, Long lastId, Integer size) {
        return feedRepository.findNextMyChannelFeeds(params.getMyChannelId(), lastId, PageRequest.of(0, size)).stream()
                .map(feed ->converterFacade.fromEntity(feed,FeedsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChannelType getSupportedChannelType() {
        return ChannelType.MY_CHANNEL;
    }
}