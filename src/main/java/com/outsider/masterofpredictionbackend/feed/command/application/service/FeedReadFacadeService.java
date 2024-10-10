package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FeedReadFacadeService {
    private final Map<ChannelType, FeedsReadService<?>> feedServices;

    public FeedReadFacadeService(List<FeedsReadService<?>> services) {
        feedServices = services.stream()
                .collect(Collectors.toMap(FeedsReadService::getSupportedChannelType, Function.identity()));
    }

    public <T> List<FeedsResponseDTO> getInitialFeeds(ChannelType type, T params, Integer size) {
        validateParams(type, params);
        FeedsReadService<T> service =  getFeedService(type);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported feed type: " + type);
        }
        return service.getInitialFeeds(params, size);
    }

    public <T> List<FeedsResponseDTO> getNextFeeds(ChannelType type, T params, Long lastId, Integer size) {
        validateParams(type, params);
        FeedsReadService<T> service = getFeedService(type);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported feed type: " + type);
        }
        return service.getNextFeeds(params, lastId, size);
    }

    private <T> void validateParams(ChannelType type, T params) {
        if (!type.getParamsClass().isInstance(params)) {
            throw new IllegalArgumentException("Invalid params type for " + type);
        }
    }
    @SuppressWarnings("unchecked")
    private <T> FeedsReadService<T> getFeedService(ChannelType type) {
        FeedsReadService<?> service = feedServices.get(type);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported feed type: " + type);
        }
        return (FeedsReadService<T>) service;
    }
}

