package com.outsider.masterofpredictionbackend.categoryChannelComment.query;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedQueryCommentsService {

    private final FeedQueryCommentsRepository feedQueryCommentsRepository;

    public FeedQueryCommentsService(FeedQueryCommentsRepository feedQueryCommentsRepository) {
        this.feedQueryCommentsRepository = feedQueryCommentsRepository;
    }

    public List<FeedQueryCommentsDTO> findBettingChannelComments(Long channelId) {
        return feedQueryCommentsRepository.findByChannelId(channelId);
    }
}
