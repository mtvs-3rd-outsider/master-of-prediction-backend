package com.outsider.masterofpredictionbackend.bettingorder.query.repository;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingOptionRepository extends JpaRepository<BettingProductOption, Long> {

    @Query("SELECT bo.id FROM BettingProductOption bo WHERE bo.bettingId = :bettingId")
    List<Long> findBettingOptionIdByBettingId(Long bettingId);
}
