package com.outsider.masterofpredictionbackend.ranking.command.application.service;

import com.outsider.masterofpredictionbackend.exception.NotExistException;
import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.UserRanking;
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

    public RankingService(UserRankingRepository userRankingRepository, UserCommandRepository userCommandRepository) {
        this.userRankingRepository = userRankingRepository;
        this.userCommandRepository = userCommandRepository;
    }

    @Transactional
    public void updateRanking(Long userId, BigDecimal newPoints) {
        // 사용자 랭킹 조회 또는 신규 생성
        UserRanking ranking = userRankingRepository.findById(userId).orElse(new UserRanking(userId));

        int oldRank = ranking.getRank();
        int newRank = calculateRank(newPoints);

        ranking.setPoints(newPoints);
        ranking.setRank(newRank);
        ranking.setLastUpdated(LocalDateTime.now());

        // 순위가 변경되었을 때 다른 사용자들의 순위 조정
        if (newRank < oldRank) {
            // 사용자가 상위로 이동한 경우: 그 아래의 사용자를 한 칸씩 밀기
            userRankingRepository.shiftRankingsDown(newRank, oldRank - 1);
        } else if (newRank > oldRank) {
            // 사용자가 하위로 이동한 경우: 그 위의 사용자를 한 칸씩 올리기
            userRankingRepository.shiftRankingsUp(oldRank + 1, newRank);
        }

        // 전체 사용자 수 조회
        long totalUsers = userRankingRepository.count();

        // 사용자 백분위 계산
        double percentile = ((double) (totalUsers - newRank + 1) / totalUsers) * 100;

        // 티어 결정
        Tier newTier = Tier.getTierByPercentileAndBets(percentile);
        User user = userCommandRepository.findById(userId).orElseThrow(NotExistException::new);
        user.setTier(newTier);
        user.setPoints(newPoints);
        userCommandRepository.save(user);
        userRankingRepository.save(ranking);
    }

    // 순위 계산 로직
    private int calculateRank(BigDecimal newPoints) {
        // 동일 포인트 사용자 수를 고려하여 순위 계산
        long higherPointsCount = userRankingRepository.countByPointsGreaterThan(newPoints);
        return (int)(higherPointsCount + 1);
    }
}
