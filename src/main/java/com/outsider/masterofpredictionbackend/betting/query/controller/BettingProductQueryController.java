package com.outsider.masterofpredictionbackend.betting.query.controller;


import com.outsider.masterofpredictionbackend.betting.command.application.service.BettingProductMessageCode;
import com.outsider.masterofpredictionbackend.betting.command.application.service.CustomBettingProductMessage;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO;
import com.outsider.masterofpredictionbackend.betting.query.service.BettingProductQueryService;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Tag(description = "배팅 API", name = "배팅 API")
public class BettingProductQueryController {

    private final BettingProductQueryService bettingProductQueryService;
    private final CustomBettingProductMessage messageSource;


    public BettingProductQueryController(BettingProductQueryService bettingProductQueryService, CustomBettingProductMessage messageSource) {
        this.bettingProductQueryService = bettingProductQueryService;
        this.messageSource = messageSource;
    }

    @GetMapping("/api/v1/betting-products")
    @Operation(summary = "배팅 상품 조회")
    @Parameter(name = "sort", in = ParameterIn.QUERY, hidden = true)
    public ResponseEntity<?> getBettingProducts(
            @Parameter(description = "페이지 정보", schema = @Schema(type = "object", defaultValue = "{\n \"page\": 0,\n \"size\": 10\n}"))
            @PageableDefault(page = 0, size = 10,  direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(bettingProductQueryService.all(pageable));
    }

    @GetMapping("/api/v1/betting-products/category/{categoryId}")
    @Operation(summary = "배팅 상품 조회")
    @Parameter(name = "sort", in = ParameterIn.QUERY, hidden = true)
    public ResponseEntity<?> getBettingProducts(
            @PathVariable Long categoryId,
            @Parameter(description = "페이지 정보", schema = @Schema(type = "object", defaultValue = "{\n \"page\": 0,\n \"size\": 10\n}"))
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(bettingProductQueryService.findByCategoryId(pageable, categoryId));
    }

    @GetMapping("/api/v1/betting-products/user/v2")
    @Operation(summary = "특정 유저의 상품 조회")
    @Parameter(name = "sort", in = ParameterIn.QUERY, hidden = true)
    public ResponseEntity<?> getBettingProductsByUserId(
            @RequestParam Long userId,
            @PageableDefault(page = 0, size = 10,  sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BettingViewDTO> bettingProducts = bettingProductQueryService.allByUserId(userId, pageable);
        return ResponseEntity.ok(bettingProducts);
    }

    @GetMapping("/api/v1/betting-products/{id}")
    @Operation(summary = "배팅 상품 상세 조회")
    public ResponseEntity<?> getBettingProductDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetail customUserDetail){
        return ResponseEntity.ok(bettingProductQueryService.detail(id, customUserDetail != null ? customUserDetail.getId() : null));
    }


    private Long parseUserId(String userId) {
        try {
            return Long.parseLong(userId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @GetMapping("/api/v1/user-point/{id}")
    @Operation(summary = "유저 포인트 조회")
    public ResponseEntity<?> getUserPoint(@PathVariable String id){
        Long userNum = parseUserId(id);
        if (userNum == null || userNum == 0){
            return ResponseEntity.ok(BigDecimal.ZERO);
        }
        return ResponseEntity.ok(bettingProductQueryService.findUserPoint(userNum));
    }
}
