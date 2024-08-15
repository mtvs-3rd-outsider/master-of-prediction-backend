package com.outsider.masterofpredictionbackend.betting.command.domain.repository;


import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingProductRepository extends JpaRepository<BettingProduct, Long> {

}
