package com.outsider.masterofpredictionbackend.bettingorder.query.service;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.ActivityDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.RatioDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.TopHolderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.repository.BettingOrderQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Long, List<TopHolderDTO>> findBettingTopHolders(Long bettingId) {
        List<TopHolderDTO> results = bettingOrderQueryRepository.findTopHolders(bettingId);
        return results.stream()
                .collect(Collectors.groupingBy(
                        TopHolderDTO::getBettingOptionId
                ));
    }

    public List<RatioDTO> findBettingProductOptionsRatio(Long bettingId) {
        return bettingOrderQueryRepository.findBettingProductOptionsRatio(bettingId);
    }
}
