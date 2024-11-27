package com.outsider.masterofpredictionbackend.ranking.command.domain.repository;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ScoreRankingRepository extends JpaRepository<ScoreRanking, Long> {

    // 특정 점수보다 높은 점수를 가진 사용자 수를 조회하여 순위 계산에 활용
    long countByScoreGreaterThan(int score);

    // 상위 N명의 랭킹을 조회
    @Query("SELECT s FROM ScoreRanking s ORDER BY s.score DESC, s.updatedAt ASC")
    List<ScoreRanking> findTopNByOrderByScoreDesc(int limit);

    // 특정 점수를 가진 랭킹들을 조회
    List<ScoreRanking> findByScore(int score);

    // 특정 사용자의 랭킹 정보 조회
    ScoreRanking findByUserId(Long userId);

    // 특정 점수의 순위를 조회
    @Query("SELECT COUNT(s) + 1 FROM ScoreRanking s WHERE s.score > :score")
    Long getRankByScore(int score);

    @Modifying
    @Transactional
    @Query("UPDATE ScoreRanking s SET s.rank = s.rank + 1 WHERE s.rank BETWEEN :startRank AND :endRank")
    void shiftRankingsDown(int startRank, int endRank);

    @Modifying
    @Transactional
    @Query("UPDATE ScoreRanking s SET s.rank = s.rank - 1 WHERE s.rank BETWEEN :startRank AND :endRank")
    void shiftRankingsUp(int startRank, int endRank);
}
