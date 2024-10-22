package com.outsider.masterofpredictionbackend.ranking.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RankingRepositoryCustom {
    Page<UserRankingDTO> findUserRankingsWithUserName(Pageable pageable);
    Optional<UserRankingDTO> findUserRankingByUserId(Long userId);
}

