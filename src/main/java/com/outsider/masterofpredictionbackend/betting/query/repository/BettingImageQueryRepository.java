package com.outsider.masterofpredictionbackend.betting.query.repository;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingImageQueryRepository extends JpaRepository<BettingProductImage, Long> {

    @Query("select bpi from BettingProductImage bpi where bpi.bettingId in (:ids)")
    List<BettingProductImage> findAllByIds(List<Long> ids);

    @Query("select bpi.imgUrl from BettingProductImage bpi where bpi.bettingId = :id")
    List<String> findByBettingId(Long id);
}
