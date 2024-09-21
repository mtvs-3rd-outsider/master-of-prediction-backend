package com.outsider.masterofpredictionbackend.betting.query.repository;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingOptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingOptionQueryRepository extends JpaRepository<BettingProductOption, Long> {

    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingOptionDTO(" +
            "bpo.id, bpo.content, bpo.imgUrl" +
            ") FROM BettingProductOption bpo WHERE bpo.bettingId = :id")
    List<BettingOptionDTO> findByBettingId(Long id);
}
