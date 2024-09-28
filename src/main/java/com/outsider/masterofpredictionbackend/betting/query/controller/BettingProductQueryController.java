package com.outsider.masterofpredictionbackend.betting.query.controller;


import com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO;
import com.outsider.masterofpredictionbackend.betting.query.service.BettingProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(description = "배팅 API", name = "배팅 API")
public class BettingProductQueryController {

    private final BettingProductQueryService bettingProductQueryService;

    public BettingProductQueryController(BettingProductQueryService bettingProductQueryService) {
        this.bettingProductQueryService = bettingProductQueryService;
    }

    @GetMapping("/api/v1/betting-products")
    @Operation(summary = "배팅 상품 조회")
    public ResponseEntity<?> getBettingProducts(){
        return ResponseEntity.ok(bettingProductQueryService.all());
    }

    @GetMapping("/api/v1/betting-products/user")
    @Operation(summary = "특정 유저의 상품 조회")
    public ResponseEntity<?> getBettingProductsByUserId(@RequestParam Long userId){
        return ResponseEntity.ok(bettingProductQueryService.allByUserId(userId));
    }
    @GetMapping("/api/v1/betting-products/user/v2")
    @Operation(summary = "특정 유저의 상품 조회")
    public ResponseEntity<?> getBettingProductsByUserId(
            @RequestParam Long userId,
            @PageableDefault(page = 0, size = 10,  direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BettingViewDTO> bettingProducts = bettingProductQueryService.allByUserId(userId, pageable);
        return ResponseEntity.ok(bettingProducts);
    }

    @GetMapping("/api/v1/betting-products/{id}")
    @Operation(summary = "배팅 상품 상세 조회")
    public ResponseEntity<?> getBettingProductDetail(@PathVariable Long id){
        return ResponseEntity.ok(bettingProductQueryService.detail(id));
    }
    
    @GetMapping("/api/v1/user-point/{id}")
    @Operation(summary = "유저 포인트 조회")
    public ResponseEntity<?> getUserPoint(@PathVariable Long id){
        return ResponseEntity.ok(bettingProductQueryService.findUserPoint(id));
    }
}
