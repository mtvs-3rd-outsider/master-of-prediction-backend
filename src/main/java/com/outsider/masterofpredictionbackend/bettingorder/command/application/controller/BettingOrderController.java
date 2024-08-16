package com.outsider.masterofpredictionbackend.bettingorder.command.application.controller;

import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.service.BettingOrderCommandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/betting-orders")
public class BettingOrderController {

    private BettingOrderCommandService bettingOrderCommandService;

    @Autowired
    public BettingOrderController(BettingOrderCommandService bettingOrderCommandService) {
        this.bettingOrderCommandService = bettingOrderCommandService;
    }


    @PostMapping("/buy/{bettingProductId}")
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
            return ResponseEntity.badRequest().build();
        }
        bettingOrderCommandService.buyBettingProduct(bettingOrderDTO);
        return null;
    }

    @PostMapping("/sell/{bettingOrderId}")
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
            return ResponseEntity.badRequest().build();
        }
        bettingOrderCommandService.sellBettingProduct(bettingOrderDTO);
        return null;
    }

}
