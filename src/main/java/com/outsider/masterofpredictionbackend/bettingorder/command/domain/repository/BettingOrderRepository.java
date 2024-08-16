package com.outsider.masterofpredictionbackend.bettingorder.command.domain.repository;


import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingOrderRepository extends JpaRepository<BettingOrder, Long> {
}
