package com.outsider.masterofpredictionbackend.betting.command.application.controller;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionFormDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.service.ProductCommandService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping
@Validated
@Tag(name = "배팅 API", description = "배팅 API")
public class BettingProductController {

    private final ProductCommandService productCommandService;

    @Autowired
    public BettingProductController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping("/api/v1/betting-products")
    @Operation(summary = "배팅 상품 등록")
    public ResponseEntity<?> save(
               @Valid @ModelAttribute BettingProductAndOptionDTO bettingProductAndOptionDTO,
           @Valid @ModelAttribute BettingProductOptionFormDTO bettingProductOptionFormDTO,
            BindingResult bindingResult,
            @UserId CustomUserInfoDTO customUserInfo
            ){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            LocalDateTime dateTime = bettingProductAndOptionDTO.getDeadLineDateTime();
            // 날짜와 시간 분리
            bettingProductAndOptionDTO.setDeadlineDate(dateTime.toLocalDate());
            bettingProductAndOptionDTO.setDeadlineTime(dateTime.toLocalTime());

        } catch (Exception e) {
            // 예외 발생 시 처리
            return new ResponseEntity<>(Map.of("error","error not 'yyyy-MM-ddTHH:mm'"), HttpStatus.BAD_REQUEST);
        }
        int size = bettingProductOptionFormDTO.getOptions_image().size();
        if (bettingProductOptionFormDTO.getOptions_image().size() != bettingProductOptionFormDTO.getOptions_content().size() || size < 2) {
            return new ResponseEntity<>(Map.of("error","options_image and options_content size must be same"), HttpStatus.BAD_REQUEST);
        }
        bettingProductAndOptionDTO.setOptions(new ArrayList<>(size));
        for (int i = 0; i < size; i++) {
            bettingProductAndOptionDTO.getOptions().add(new BettingProductOptionDTO(bettingProductOptionFormDTO.getOptions_content().get(i), bettingProductOptionFormDTO.getOptions_image().get(i)));
        }
        // NOTE: 임시 데이터
        bettingProductAndOptionDTO.setUserId(customUserInfo.getUserId());
        // bettingProductAndOptionDTO.setUserId(1L);
        Long productId;
        try {
            productId = productCommandService.save(bettingProductAndOptionDTO);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(Map.of("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }
}
