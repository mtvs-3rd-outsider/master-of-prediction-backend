package com.outsider.masterofpredictionbackend.bettingorder.query.repository;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.BettingOrderHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingOrderQueryRepository extends JpaRepository<BettingOrder, Long> {


    @Query("SELECT new com.outsider.masterofpredictionbackend.bettingorder.query.dto.BettingOrderHistoryDTO(" +
            "bo.bettingOptionId, SUM(bo.point)) " +
            "FROM BettingOrder bo WHERE bo.userId = :userId AND bo.bettingId = :bettingId GROUP BY bo.bettingOptionId")
    List<BettingOrderHistoryDTO> findUserOrderHistory(Long userId, Long bettingId);
}
