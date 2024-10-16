package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.DTOConverterFacade;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.FeedConstants;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
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

    @Autowired
    public HotTopicChannelFeedService(FeedRepository feedRepository, DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
    }


    public Page<FeedsResponseDTO> getFeeds(Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findAll(pageable);
        return feedPage.map(feed ->converterFacade.fromEntity(feed,FeedsResponseDTO.class));
    }
}