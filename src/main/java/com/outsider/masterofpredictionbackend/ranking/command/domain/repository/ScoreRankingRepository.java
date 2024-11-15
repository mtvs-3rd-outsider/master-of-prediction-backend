package com.outsider.masterofpredictionbackend.ranking.command.domain.repository;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ScoreRankingRepository extends JpaRepository<ScoreRanking, Long> {

    // 특정 점수보다 높은 점수를 가진 사용자 수를 조회하여 순위 계산에 활용
    long countByScoreGreaterThan(int score);

    @Modifying
    @Transactional
    @Query("UPDATE ScoreRanking s SET s.rank = s.rank + 1 WHERE s.rank BETWEEN :startRank AND :endRank")
    void shiftRankingsDown(int startRank, int endRank);

    @Modifying
    @Transactional
    @Query("UPDATE ScoreRanking s SET s.rank = s.rank - 1 WHERE s.rank BETWEEN :startRank AND :endRank")
    void shiftRankingsUp(int startRank, int endRank);
}
