package com.outsider.masterofpredictionbackend.bettingorder.command.domain.repository;


import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BettingOrderRepository extends JpaRepository<BettingOrder, Long> {

    Collection<Object> findByBettingId(Long bettingId);



    @Query("SELECT bo.userId FROM BettingOrder bo WHERE bo.bettingId = :productId group by bo.userId")
    List<Long> findUserIdsByBettingId(Long productId);
}
