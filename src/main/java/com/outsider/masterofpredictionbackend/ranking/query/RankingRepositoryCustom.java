package com.outsider.masterofpredictionbackend.ranking.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.UserRanking;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RankingRepositoryCustom {
    Page<UserRankingDTO> findUserRankingsWithUserName(Pageable pageable);
    Optional<UserRankingDTO> findUserRankingByUserId(Long userId);
    List<UserRanking> findByPoints(BigDecimal points);

}

