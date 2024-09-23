package com.outsider.masterofpredictionbackend.bettingChannelComment.command.query;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BettingQueryCommentsService {

    private final BettingQueryCommentsRepository bettingQueryCommentsRepository;

    public BettingQueryCommentsService(BettingQueryCommentsRepository bettingQueryCommentsRepository) {
        this.bettingQueryCommentsRepository = bettingQueryCommentsRepository;
    }

    public List<BettingQueryCommentsDTO> findBettingChannelComments(Long bettingChannelId) {
        return bettingQueryCommentsRepository.findByBettingChannelId(bettingChannelId);
    }
}
