package com.outsider.masterofpredictionbackend.bettingorder.query.controller;

import com.outsider.masterofpredictionbackend.betting.command.application.service.BettingProductMessageCode;
import com.outsider.masterofpredictionbackend.betting.command.application.service.CustomBettingProductMessage;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.TopHolderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.service.BettingOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Tag(name = "배팅 주문 API", description = "배팅 주문 API")
public class BettingOrderQueryController {

    private final BettingOrderQueryService bettingOrderQueryService;
    private final CustomBettingProductMessage messageSource;

    public BettingOrderQueryController(BettingOrderQueryService bettingOrderQueryService, CustomBettingProductMessage messageSource) {
        this.bettingOrderQueryService = bettingOrderQueryService;

        this.messageSource = messageSource;
    }

    private Long parseUserId(String userId) {
        try {
            return Long.parseLong(userId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @GetMapping("/api/v1/user/betting-products")
    @Operation(summary = "유저의 구매내역 조회")
    public ResponseEntity<?> getUserOrderHistory(@RequestParam String userId, @RequestParam Long bettingId){
        Long userNum = parseUserId(userId);

        if (userNum == null || userNum == 0){
            return ResponseEntity.ok(Collections.emptyList());
        }

        if (bettingId == null){
            return ResponseEntity.badRequest().body(messageSource.getMessage(BettingProductMessageCode.NOT_FOUND, (Object) null));
        }
        return ResponseEntity.ok(bettingOrderQueryService.findUserOrderHistory(userNum, bettingId));
    }

    @GetMapping("/api/v1/betting-products/orders")
    @Operation(summary = "모든 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistory(@RequestParam Long bettingId, @RequestParam String timeRange){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistory(bettingId, timeRange));
    }

    @GetMapping("/api/v1/betting-products/activity")
    @Operation(summary = "배팅 상품의 구매내역 조회(활동 내역)")
    public ResponseEntity<?> getBettingActivity(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingActivity(bettingId));
    }

    @GetMapping("/api/v1/betting-products/top-holders")
    @Operation(summary = "배팅 상품의 구매내역 조회(보유자 순위)")
    public ResponseEntity<Map<Long, List<TopHolderDTO>>> getBettingTopHolders(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingTopHolders(bettingId));
    }

    @GetMapping("/api/v1/betting-products/options/ratio")
    @Operation(summary = "배팅 상품 옵션 비율 조회")
    public ResponseEntity<?> getBettingProductOptions(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingProductOptionsRatio(bettingId));
    }
}
