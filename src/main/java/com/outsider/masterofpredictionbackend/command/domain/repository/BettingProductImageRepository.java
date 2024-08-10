package com.outsider.masterofpredictionbackend.command.domain.repository;

import com.outsider.masterofpredictionbackend.command.domain.aggregate.BettingProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingProductImageRepository extends JpaRepository<BettingProductImage, Long> {
}
