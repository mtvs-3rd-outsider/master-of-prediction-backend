package com.outsider.masterofpredictionbackend.betting.command.domain.repository;


import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingProductRepository extends JpaRepository<BettingProduct, Long> {

    @Query("select bp from BettingProduct bp " +
            "         where bp.apiGameId IS NOT NULL " +
            "                   And bp.winningOption is null " +
            "                   And (bp.deadlineDate < now() " +
            "                   or bp.deadlineDate = now() And bp.deadlineTime < now())")
    List<BettingProduct> findByScheduledBettingProduct();
}
