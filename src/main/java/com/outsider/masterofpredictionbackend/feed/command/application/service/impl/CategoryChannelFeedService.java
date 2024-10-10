package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedsReadService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.CategoryChannelParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryChannelFeedService implements FeedsReadService<CategoryChannelParams> {

    private final FeedRepository feedRepository;
    private final DTOConverterFacade converterFacade;

    public CategoryChannelFeedService(FeedRepository feedRepository, DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
    }

    @Override
    public List<FeedsResponseDTO> getInitialFeeds(CategoryChannelParams params, Integer size) {
        return feedRepository.findChannelFeeds(ChannelType.CATEGORY, params.getCategoryId(), PageRequest.of(0, size)).stream()
                .map(feed -> converterFacade.fromEntity(feed, FeedsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedsResponseDTO> getNextFeeds(CategoryChannelParams params, Long lastId, Integer size) {
        return feedRepository.findNextChannelFeeds(ChannelType.CATEGORY, params.getCategoryId(), lastId, PageRequest.of(0, size)).stream()
                .map(feed -> converterFacade.fromEntity(feed, FeedsResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChannelType getSupportedChannelType() {
        return ChannelType.CATEGORY;
    }
}