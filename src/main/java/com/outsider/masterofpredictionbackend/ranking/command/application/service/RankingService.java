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

        int oldRank = ranking.getRank();
        int newRank = calculateRank(newPoints);

        ranking.setPoints(newPoints);
        ranking.setRank(newRank);
        ranking.setLastUpdated(LocalDateTime.now());

        // 동일한 포인트를 가진 사용자들의 순위 조정
        if (newRank != oldRank) {
            userRankingRepository.findByPoints(newPoints)
                    .forEach(r -> {
                        if (!r.getUserId().equals(userId)) {
                            r.setRank(newRank);
                            userRankingRepository.save(r);
                        }
                    });
        }

        User user = userCommandRepository.findById(userId).orElseThrow(NotExistException::new);
        user.setPoints(newPoints);
        userCommandRepository.save(user);
        userRankingRepository.save(ranking);
    }

    private int calculateRank(BigDecimal newPoints) {
        long higherPointsCount = userRankingRepository.countByPointsGreaterThan(newPoints);
        return (int)(higherPointsCount + 1);
    }

    @Transactional
    public void updateRankingByScore(Long userId, String result) {
        ScoreRanking ranking = scoreRankingRepository.findById(userId).orElse(new ScoreRanking(userId));

        int scoreChange = calculateScoreByResult(result);
        int newScore = ranking.getScore() + scoreChange;
        ranking.setScore(newScore);
        ranking.setLastUpdated(LocalDateTime.now());

        int oldRank = ranking.getRank();
        int newRank = calculateRankByScore(newScore);

        ranking.setRank(newRank);

        // 동일한 점수를 가진 사용자들의 순위 조정
        if (newRank != oldRank) {
            scoreRankingRepository.findByScore(newScore)
                    .forEach(r -> {
                        if (!r.getUserId().equals(userId)) {
                            r.setRank(newRank);
                            scoreRankingRepository.save(r);
                        }
                    });
        }

        long totalUsers = scoreRankingRepository.count();
        double percentile = ((double) (totalUsers - newRank + 1) / totalUsers) * 100;

        Tier newTier = Tier.getTierByPercentileAndBets(percentile);
        User user = userCommandRepository.findById(userId).orElseThrow(NotExistException::new);
        user.setTier(newTier);

        userCommandRepository.save(user);
        scoreRankingRepository.save(ranking);
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
