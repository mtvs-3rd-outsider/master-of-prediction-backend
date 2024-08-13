package com.outsider.masterofpredictionbackend.betting.command.domain.repository;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingProductOptionRepository extends JpaRepository<BettingProductOption, Long> {
}
