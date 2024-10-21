package com.outsider.masterofpredictionbackend.user.query.tier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserPointsChangeConsumer {
    private final RankingService rankingService;
    private final ObjectMapper objectMapper;
    public UserPointsChangeConsumer(RankingService rankingService, ObjectMapper objectMapper) {
        this.rankingService = rankingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "settlementAfterNewPoint", groupId = "ranking-group")
    public void consumeUserPointsChangeEvent(String message ) throws JsonProcessingException {
        System.out.println("Received notification: " + message);
        // Kafka에서 수신한 이벤트를 처리하여 부분적으로 랭킹 업데이트
        UserPointsChangeEvent userPointsChangeEvent = objectMapper.readValue(message, UserPointsChangeEvent.class);
        rankingService.updateRanking(userPointsChangeEvent.getUserId(), userPointsChangeEvent.getNewPoints());
    }
}
