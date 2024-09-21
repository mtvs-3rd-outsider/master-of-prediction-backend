package com.outsider.masterofpredictionbackend.bettingorder.query.controller;

import com.outsider.masterofpredictionbackend.bettingorder.query.service.BettingOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "배팅 API", description = "배팅 API")
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

}
