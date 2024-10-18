package com.outsider.masterofpredictionbackend.bettingorder.command.domain.repository;


import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BettingOrderRepository extends JpaRepository<BettingOrder, Long> {

    Collection<Object> findByBettingId(Long bettingId);


    @Query("select  bo.bettingOptionId, bo.userId, sum(bo.point) from BettingOrder bo where bo.bettingId = :productId group by bo.bettingOptionId, bo.userId")
    List<Object[]> calculateUserOrderPointSumByProductId(Long productId);

    List<Long> findUserIdsByBettingId(Long productId);
}
