package com.outsider.masterofpredictionbackend.user.query.tier;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<UserRanking, Long> {

    List<UserRanking> findTop10ByOrderByPointsDesc();

    long countByPointsGreaterThan(int points);

    List<UserRanking> findByRankBetween(int startRank, int endRank);

    // 순위를 하향 조정 (rank를 1씩 증가)
    @Modifying
    @Query("UPDATE UserRanking u SET u.rank = u.rank + 1 WHERE u.rank >= ?1 AND u.rank <= ?2")
    void shiftRankingsDown(int lowerBound, int upperBound);

    // 순위를 상향 조정 (rank를 1씩 감소)
    @Modifying
    @Query("UPDATE UserRanking u SET u.rank = u.rank - 1 WHERE u.rank >= ?1 AND u.rank <= ?2")
    void shiftRankingsUp(int lowerBound, int upperBound);
}
