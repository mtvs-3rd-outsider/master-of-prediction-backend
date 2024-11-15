package com.outsider.masterofpredictionbackend.ranking.command.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.UserPredictionResultDTO;
import com.outsider.masterofpredictionbackend.ranking.command.application.dto.UserPointsChangeEvent;
import com.outsider.masterofpredictionbackend.ranking.command.application.service.RankingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
    @KafkaListener(topics = "predictionResult", groupId = "ranking-group")
    public void consumePredictionResult(String message) throws JsonProcessingException {
        System.out.println("Received prediction result: " + message);
        List<UserPredictionResultDTO> results = Arrays.asList(objectMapper.readValue(message, UserPredictionResultDTO[].class));
        for (UserPredictionResultDTO result : results) {
            // 각 결과에 따른 점수 기반 랭킹 업데이트
            rankingService.updateRankingByScore(result.getUserId(), result.getResult());
        }
    }

}
