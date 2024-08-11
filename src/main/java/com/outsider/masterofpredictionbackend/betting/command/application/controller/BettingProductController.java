package com.outsider.masterofpredictionbackend.betting.command.application.controller;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.service.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/betting-products")
@Validated
public class BettingProductController {

    private final ProductCommandService productCommandService;

    @Autowired
    public BettingProductController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody BettingProductAndOptionDTO bettingProductAndOptionDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(productCommandService.save(bettingProductAndOptionDTO), HttpStatus.CREATED);
    }
}
