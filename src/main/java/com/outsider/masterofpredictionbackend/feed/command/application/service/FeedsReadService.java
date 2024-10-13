package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;

import java.util.List;

public interface FeedsReadService<T> {
    List<FeedsResponseDTO> getInitialFeeds(T params,Integer size);

    List<FeedsResponseDTO> getNextFeeds(T params,Long lastId,Integer size);

    ChannelType getSupportedChannelType();
}