package com.outsider.masterofpredictionbackend.bettingorder.query.repository;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.ActivityDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.BettingOrderHistoryDTO;
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
            "WHERE bo.bettingId = :bettingId")
    List<ActivityDTO> findActivity(Long bettingId);



    List<BettingOrder> findTopHolders(Long bettingId);
}
