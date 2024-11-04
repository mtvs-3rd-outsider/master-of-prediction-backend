package com.outsider.masterofpredictionbackend.bettingorder.query.controller;

import com.outsider.masterofpredictionbackend.bettingorder.query.dto.TopHolderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.query.service.BettingOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Tag(name = "배팅 주문 API", description = "배팅 주문 API")
public class BettingOrderQueryController {

    private final BettingOrderQueryService bettingOrderQueryService;

    public BettingOrderQueryController(BettingOrderQueryService bettingOrderQueryService) {
        this.bettingOrderQueryService = bettingOrderQueryService;

    }

    @GetMapping("/api/v1/user/betting-products")
    @Operation(summary = "유저의 구매내역 조회")
    public ResponseEntity<?> getUserOrderHistory(@RequestParam Long userId, @RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findUserOrderHistory(userId, bettingId));
    }

    @GetMapping("/api/v1/betting-products/orders")
    @Operation(summary = "모든 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistory(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistoryAll(bettingId));
    }

    @GetMapping("/api/v1/betting-products/options/last-hour-statistics")
    @Operation(summary = "한 시간 동안의 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistoryInLastHour(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistoryInLastHour(bettingId));
    }

    @GetMapping("/api/v1/betting-products/options/last-6hour-statistics")
    @Operation(summary = "여섯 시간 동안의 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistoryInLast6Hour(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistoryInLast6Hour(bettingId));
    }

    @GetMapping("/api/v1/betting-products/options/one-day-statistics")
    @Operation(summary = "하루 동안의 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistoryInOneDay(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistoryInOneDay(bettingId));
    }

    @GetMapping("/api/v1/betting-products/options/one-week-statistics")
    @Operation(summary = "일주일 동안의 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistoryInOneWeek(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistoryInOneWeek(bettingId));
    }


    @GetMapping("/api/v1/betting-products/options/one-month-statistics")
    @Operation(summary = "한달 동안의 배팅 상품 구매내역 조회(그래프)")
    public ResponseEntity<?> getBettingOrderHistoryInOneMonth(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingOrderQueryService.findBettingOrderHistoryInOneMonth(bettingId));
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
