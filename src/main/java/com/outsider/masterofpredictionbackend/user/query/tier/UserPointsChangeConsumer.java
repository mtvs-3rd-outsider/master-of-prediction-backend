package com.outsider.masterofpredictionbackend.user.query.tier;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserPointsChangeConsumer {
    private final RankingService rankingService;

    public UserPointsChangeConsumer(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @KafkaListener(topics = "exact-calculation", groupId = "ranking-group")
    public void consumeUserPointsChangeEvent(UserPointsChangeEvent event) {
        // Kafka에서 수신한 이벤트를 처리하여 부분적으로 랭킹 업데이트
        rankingService.updateRanking(event.getUserId(), event.getNewPoints());
    }
}
