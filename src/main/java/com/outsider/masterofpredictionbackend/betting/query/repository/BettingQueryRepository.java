package com.outsider.masterofpredictionbackend.betting.query.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingDetailDTO;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO;

public interface BettingQueryRepository extends JpaRepository<BettingProduct, Long> {

    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO( " +
            "u.id, u.userName, u.displayName, u.tier.name, u.userImg, bp.title, bp.id, " +
            "bp.isBlind, bp.blindName, bp.createdAt, " +
            "c.id, c.displayName, c.categoryChannelStatus, u.authority) " +
            "FROM BettingProduct bp " +
            "JOIN User u ON bp.userId = u.id " +
            "LEFT JOIN CategoryChannel c ON bp.categoryCode = c.id " +
            "ORDER BY bp.id DESC")
    Page<BettingViewDTO> findBetting(Pageable pageable);

    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO( " +
            "u.id, u.userName, u.displayName, u.tier.name, u.userImg, bp.title, bp.id, " +
            "bp.isBlind, bp.blindName, bp.createdAt, " +
            "c.id, c.displayName, c.categoryChannelStatus, u.authority) " +
            "FROM BettingProduct bp " +
            "JOIN User u ON bp.userId = u.id " +
            "LEFT JOIN CategoryChannel c ON bp.categoryCode = c.id " +
            "WHERE bp.categoryCode = :categoryId " +
            "ORDER BY bp.id DESC")
    Page<BettingViewDTO> findByCategoryId(Pageable pageable, Long categoryId);

    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO( " +
            "u.id, u.userName, u.displayName, u.tier.name, u.userImg, bp.title, bp.id, " +
            "bp.isBlind, bp.blindName, bp.createdAt, " +
            "c.id, c.displayName, c.categoryChannelStatus, u.authority) " +
            "FROM BettingProduct bp " +
            "JOIN User u ON bp.userId = u.id " +
            "LEFT JOIN CategoryChannel c ON bp.categoryCode = c.id " +
            "WHERE u.id = :userId AND bp.isBlind = false")
    Page<BettingViewDTO> findBettingByUserId(Long userId, Pageable pageable);

    @Query("SELECT new com.outsider.masterofpredictionbackend.betting.query.dto.BettingDetailDTO(" +
            " new com.outsider.masterofpredictionbackend.betting.query.dto.BettingUserDTO(u.id, u.userName, u.displayName, u.tier.name, u.userImg, u.authority), " +
            " new com.outsider.masterofpredictionbackend.betting.query.dto.BettingContentDTO(bp.deadlineDate, bp.deadlineTime, bp.isBlind, bp.title, bp.content, bp.userId, bp.blindName, bp.winningOption) " +
            ") FROM BettingProduct bp JOIN User u ON bp.userId = u.id WHERE bp.id = :id")
    BettingDetailDTO findBettingById(Long id);

}