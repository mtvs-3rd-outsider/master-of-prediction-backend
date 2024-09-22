package com.outsider.masterofpredictionbackend.bettingorder.query.service;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.ActivityDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.TopHolderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.repository.BettingOrderQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BettingOrderQueryService {

    private final BettingOrderQueryRepository bettingOrderQueryRepository;

    public BettingOrderQueryService(BettingOrderQueryRepository bettingOrderQueryRepository) {
        this.bettingOrderQueryRepository = bettingOrderQueryRepository;
    }

    public Object findUserOrderHistory(Long userId, Long bettingId){
        return bettingOrderQueryRepository.findUserOrderHistory(userId, bettingId);
    }

    public List<ActivityDTO> findBettingActivity(Long bettingId) {
        return bettingOrderQueryRepository.findActivity(bettingId);
    }

    public List<TopHolderDTO> findBettingTopHolders(Long bettingId) {
        List<BettingOrder> topHolders = bettingOrderQueryRepository.findTopHolders(bettingId);
        return null;
    }
}
