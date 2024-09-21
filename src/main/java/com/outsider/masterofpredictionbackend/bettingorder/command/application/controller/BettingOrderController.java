package com.outsider.masterofpredictionbackend.bettingorder.command.application.controller;

import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.service.BettingOrderCommandService;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/betting-orders")
@Tag(name = "배팅 API")
@Slf4j
public class BettingOrderController {

    private BettingOrderCommandService bettingOrderCommandService;

    @Autowired
    public BettingOrderController(BettingOrderCommandService bettingOrderCommandService) {
        this.bettingOrderCommandService = bettingOrderCommandService;
    }


    @PostMapping("/buy/{bettingProductId}")
    @Operation(summary = "배팅 상품 구매")
    public ResponseEntity<?> buyBettingProduct(
            @Valid @RequestBody BettingOrderDTO bettingOrderDTO,
            BindingResult bindingResult
            ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        log.debug("bettingOrderDTO: {}", bettingOrderDTO);
        BettingOrder bettingOrder = bettingOrderCommandService.buyBettingProduct(bettingOrderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bettingOrder);
    }

    @PostMapping("/sell/{bettingOrderId}")
    @Operation(summary = "배팅 상품 판매")
    public ResponseEntity<?> sellBettingProduct(
            @Valid @RequestBody BettingOrderDTO bettingOrderDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        BettingOrder bettingOrder = bettingOrderCommandService.sellBettingProduct(bettingOrderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bettingOrder);

    }

}
