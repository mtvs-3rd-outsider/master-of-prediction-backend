package com.outsider.masterofpredictionbackend.bettingorder.command.application.service;

import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.BettingOrderService;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.UserPoint;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class BettingOrderCommandService {

    private final UserPoint userPoint;
    private final BettingOrderService bettingOrderService;

    @Autowired
    public BettingOrderCommandService(UserPoint userPoint, BettingOrderService bettingOrderService) {
        this.userPoint = userPoint;
        this.bettingOrderService = bettingOrderService;
    }


    @Transactional
    public void buyBettingProduct(BettingOrderDTO bettingOrderDTO) {
        validatePoint(bettingOrderDTO.getPoint(), bettingOrderDTO.getUserId());
        userPoint.pointUpdate(bettingOrderDTO.getUserId(), bettingOrderDTO.getPoint().negate());
        bettingOrderService.save(new BettingOrder(
                bettingOrderDTO.getUserId(),
                bettingOrderDTO.getBettingId(),
                bettingOrderDTO.getPoint(),
                bettingOrderDTO.getBettingOptionId(),
                LocalDate.now(),
                LocalTime.now()
        ));
    }

    @Transactional
    public void sellBettingProduct(BettingOrderDTO bettingOrderDTO) {
        validatePoint(bettingOrderDTO.getPoint(), bettingOrderDTO.getUserId());
        userPoint.pointUpdate(bettingOrderDTO.getUserId(), bettingOrderDTO.getPoint());
        bettingOrderService.save(new BettingOrder(
                bettingOrderDTO.getUserId(),
                bettingOrderDTO.getBettingId(),
                bettingOrderDTO.getPoint(),
                bettingOrderDTO.getBettingOptionId(),
                LocalDate.now(),
                LocalTime.now()
        ));
    }

    private void validatePoint(BigDecimal point, Long UserId) {
        if (point.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("point less than or equal to zero");
        }
        if (userPoint.find(UserId).compareTo(point) < 0) {
            throw new IllegalArgumentException("point is not enough");
        }
    }
}
