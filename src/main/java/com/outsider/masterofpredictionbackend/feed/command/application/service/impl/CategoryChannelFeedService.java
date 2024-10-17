package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.CategoryChannelParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryChannelFeedService {

    private final FeedRepository feedRepository;
    private final DTOConverterFacade converterFacade;

    public CategoryChannelFeedService(FeedRepository feedRepository, DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
    }



}