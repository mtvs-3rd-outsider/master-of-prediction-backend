package com.outsider.masterofpredictionbackend.bettingorder.query.repository;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.ActivityDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.BettingOrderHistoryDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.RatioDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.TopHolderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingOrderQueryRepository extends JpaRepository<BettingOrder, Long> {


    @Query("SELECT new com.outsider.masterofpredictionbackend.bettingorder.query.dto.BettingOrderHistoryDTO(" +
            "bo.bettingOptionId, SUM(bo.point)) " +
            "FROM BettingOrder bo WHERE bo.userId = :userId AND bo.bettingId = :bettingId GROUP BY bo.bettingOptionId")
    List<BettingOrderHistoryDTO> findUserOrderHistory(Long userId, Long bettingId);

    @Query("SELECT new com.outsider.masterofpredictionbackend.bettingorder.query.dto.ActivityDTO(u.userName, " +
            "       u.displayName, " +
            "       u.tier.name, " +
            "       u.userImg, " +
            "       bo.point, " +
            "       bo.orderDate, " +
            "       bo.orderTime, " +
            "       bpo.content )" +
            "FROM BettingOrder as bo " +
            "         join User as u on u.id = bo.userId " +
            "         join BettingProductOption as bpo on bo.bettingOptionId = bpo.id " +
            "WHERE bo.bettingId = :bettingId ORDER BY bo.orderDate DESC, bo.orderTime DESC")
    List<ActivityDTO> findActivity(Long bettingId);


    @Query("SELECT new com.outsider.masterofpredictionbackend.bettingorder.query.dto.TopHolderDTO(" +
            "    u.userName, " +
            "    u.displayName, " +
            "    u.tier.name, " +
            "    u.userImg, " +
            "    sum(bo.point), " +
            "    bo.bettingOptionId)" +
            "FROM BettingOrder bo " +
            "    join User as u on bo.userId=u.id " +
            "group by bo.userId, bo.bettingOptionId " +
            "HAVING SUM(bo.point) > 0 ORDER BY bo.bettingOptionId , SUM(bo.point) DESC")
    List<TopHolderDTO> findTopHolders(Long bettingId);

    @Query("WITH grouped_bets AS ( " +
            "    SELECT " +
            "        bo.bettingOptionId AS bettingOptionId, " +
            "        SUM(bo.point) AS totalPoints " +
            "    FROM " +
            "        BettingOrder bo " +
            "    WHERE " +
            "        bo.bettingId = :bettingId " +
            "    GROUP BY " +
            "        bo.bettingOptionId " +
            "), " +
            "total_sum AS ( " +
            "    SELECT " +
            "        SUM(gb.totalPoints) AS overallTotal " +
            "    FROM " +
            "        grouped_bets gb " +
            ") " +
            "SELECT NEW com.outsider.masterofpredictionbackend.bettingorder.query.dto.RatioDTO( " +
            "    gb.bettingOptionId, " +
            "    gb.totalPoints, " +
            "    CONCAT(CAST(FLOOR((gb.totalPoints * 100.0) / ts.overallTotal) AS string), '%') " +
            ") " +
            "FROM " +
            "    grouped_bets gb, " +
            "    total_sum ts " +
            "ORDER BY " +
            "    gb.bettingOptionId")
    List<RatioDTO> findBettingProductOptionsRatio(Long bettingId);
}
