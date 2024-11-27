package com.outsider.masterofpredictionbackend.ranking.command.application.service;

import com.outsider.masterofpredictionbackend.exception.NotExistException;
import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.UserRanking;
import com.outsider.masterofpredictionbackend.ranking.command.domain.repository.ScoreRankingRepository;
import com.outsider.masterofpredictionbackend.ranking.command.domain.repository.UserRankingRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Tier;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RankingService {
    private final UserRankingRepository userRankingRepository;
    private final UserCommandRepository userCommandRepository;
    private final ScoreRankingRepository scoreRankingRepository;
    public RankingService(UserRankingRepository userRankingRepository, UserCommandRepository userCommandRepository, ScoreRankingRepository scoreRankingRepository) {
        this.userRankingRepository = userRankingRepository;
        this.userCommandRepository = userCommandRepository;
        this.scoreRankingRepository = scoreRankingRepository;
    }

    @Transactional
    public void updateRanking(Long userId, BigDecimal newPoints) {
        UserRanking ranking = userRankingRepository.findById(userId).orElse(new UserRanking(userId));
        
        ranking.setPoints(newPoints);
        ranking.setLastUpdated(LocalDateTime.now());

        // 전체 순위 재계산
        updateAllRankings();

        User user = userCommandRepository.findById(userId).orElseThrow(NotExistException::new);
        user.setPoints(newPoints);
        userCommandRepository.save(user);
        userRankingRepository.save(ranking);
    }

    private void updateAllRankings() {
        // 포인트 내림차순으로 모든 랭킹을 조회
        List<UserRanking> allRankings = userRankingRepository.findAllByOrderByPointsDescLastUpdatedAsc();
        
        int currentRank = 1;
        BigDecimal previousPoints = null;
        int sameRankCount = 0;

        for (UserRanking ranking : allRankings) {
            if (previousPoints != null && ranking.getPoints().compareTo(previousPoints) != 0) {
                // 이전 포인트와 다른 경우, 중복된 순위만큼 건너뛴 순위 부여
                currentRank += sameRankCount;
                sameRankCount = 0;
            }
            
            ranking.setRank(currentRank);
            userRankingRepository.save(ranking);
            
            previousPoints = ranking.getPoints();
            sameRankCount++;
        }
    }

    @Transactional
    public void updateRankingByScore(Long userId, String result) {
        ScoreRanking ranking = scoreRankingRepository.findById(userId).orElse(new ScoreRanking(userId));

        int scoreChange = calculateScoreByResult(result);
        int newScore = ranking.getScore() + scoreChange;
        ranking.setScore(newScore);
        ranking.setLastUpdated(LocalDateTime.now());

        // 전체 순위 재계산
        updateAllScoreRankings();

        long totalUsers = scoreRankingRepository.count();
        double percentile = ((double) (totalUsers - ranking.getRank() + 1) / totalUsers) * 100;

        Tier newTier = Tier.getTierByPercentileAndBets(percentile);
        User user = userCommandRepository.findById(userId).orElseThrow(NotExistException::new);
        user.setTier(newTier);

        userCommandRepository.save(user);
        scoreRankingRepository.save(ranking);
    }

    private void updateAllScoreRankings() {
        // 점수 내림차순으로 모든 랭킹을 조회
        List<ScoreRanking> allRankings = scoreRankingRepository.findAllByOrderByScoreDescLastUpdatedAsc();
        
        int currentRank = 1;
        Integer previousScore = null;
        int sameRankCount = 0;

        for (ScoreRanking ranking : allRankings) {
            if (previousScore != null && ranking.getScore() != previousScore) {
                // 이전 점수와 다른 경우, 중복된 순위만큼 건너뛴 순위 부여
                currentRank += sameRankCount;
                sameRankCount = 0;
            }
            
            ranking.setRank(currentRank);
            scoreRankingRepository.save(ranking);
            
            previousScore = ranking.getScore();
            sameRankCount++;
        }
    }

    private int calculateScoreByResult(String result) {
        switch (result) {
            case "win": return 3;
            case "draw": return 0;
            case "lose": return -3;
            default: return 0;
        }
    }

    public int calculateRankByScore(int score) {
        long higherScoreCount = scoreRankingRepository.countByScoreGreaterThan(score);
        return (int) (higherScoreCount + 1);
    }

}
