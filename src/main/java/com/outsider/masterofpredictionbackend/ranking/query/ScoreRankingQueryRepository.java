package com.outsider.masterofpredictionbackend.ranking.query;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScoreRankingQueryRepository extends JpaRepository<ScoreRanking, Long> {

    // 사용자 ID로 점수 기반 순위 조회
    Optional<ScoreRanking> findByUserId(Long userId);

    // 전체 점수 기반 순위 목록 조회 (페이징)
    Page<ScoreRanking> findAll(Pageable pageable);
}
