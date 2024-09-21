package com.outsider.masterofpredictionbackend.betting.query.repository;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingDetailDTO;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingQueryRepository extends JpaRepository<BettingProduct, Long> {

    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO(" +
            " u.id, u.userName, u.displayName, u.tier.name, u.userImg, bp.title, bp.id, bp.isBlind)  FROM BettingProduct bp " +
            "JOIN User u ON bp.userId = u.id ORDER BY bp.id LIMIT :limit OFFSET :offset")
    List<BettingViewDTO> findBettinglimit(int limit, int offset);


    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingDetailDTO(" +
            " new com.outsider.masterofpredictionbackend.betting.query.dto.BettingUserDTO(u.id, u.userName, u.displayName, u.tier.name, u.userImg), " +
            " new com.outsider.masterofpredictionbackend.betting.query.dto.BettingContentDTO(bp.deadlineDate, bp.deadlineTime, bp.isBlind, bp.title, bp.content, bp.userId) " +
            ") FROM BettingProduct bp JOIN User u ON bp.userId = u.id WHERE bp.id = :id")
    BettingDetailDTO findBettingById(Long id);

}