package com.outsider.masterofpredictionbackend.betting.command.application.controller;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionFormDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.service.BettingProductMessageCode;
import com.outsider.masterofpredictionbackend.betting.command.application.service.CustomBettingProductMessage;
import com.outsider.masterofpredictionbackend.betting.command.application.service.ProductCommandService;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.naver.ApiBettingProductService;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.AdminUserIdList;
import com.outsider.masterofpredictionbackend.util.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping
// @Validated
@Slf4j
@Tag(name = "배팅 API", description = "배팅 API")
public class BettingProductController {

    private final ProductCommandService productCommandService;
    private final BettingProductService bettingProductService;
    private final ApiBettingProductService apiBettingProductService;
    private final CustomBettingProductMessage messageSource;

    @Autowired
    public BettingProductController(ProductCommandService productCommandService, BettingProductService bettingProductService, ApiBettingProductService apiBettingProductService, CustomBettingProductMessage messageSource) {
        this.productCommandService = productCommandService;
        this.bettingProductService = bettingProductService;
        this.apiBettingProductService = apiBettingProductService;
        this.messageSource = messageSource;
    }

    @PostMapping("/api/v1/betting-products")
    @Operation(summary = "배팅 상품 등록")
    public ResponseEntity<?> save(
                @Valid @ModelAttribute  BettingProductAndOptionDTO bettingProductAndOptionDTO,
            @Valid @ModelAttribute  BettingProductOptionFormDTO bettingProductOptionFormDTO,
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
            errors.put("message", messageSource.getMessage(BettingProductMessageCode.CREATE_FAILED, (Object) null));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            LocalDateTime dateTime = bettingProductAndOptionDTO.getDeadLineDateTime();
            // 날짜와 시간 분리
            bettingProductAndOptionDTO.setDeadlineDate(dateTime.toLocalDate());
            bettingProductAndOptionDTO.setDeadlineTime(dateTime.toLocalTime());

        } catch (Exception e) {
            // 예외 발생 시 처리;
            return new ResponseEntity<>(Map.of("message",messageSource.getMessage(BettingProductMessageCode.TIME_FORMAT_INVALID, (Object) null)), HttpStatus.BAD_REQUEST);
        }
        int size = bettingProductOptionFormDTO.getOptions_image().size();
        if (bettingProductOptionFormDTO.getOptions_image().size() != bettingProductOptionFormDTO.getOptions_content().size() || size < 2) {
            return new ResponseEntity<>(Map.of("message",messageSource.getMessage(BettingProductMessageCode.PRODUCT_OPTION_MISSING, (Object) null)), HttpStatus.BAD_REQUEST);
        }
        bettingProductAndOptionDTO.setOptions(new ArrayList<>(size));
        for (int i = 0; i < size; i++) {
            bettingProductAndOptionDTO.getOptions().add(new BettingProductOptionDTO(bettingProductOptionFormDTO.getOptions_content().get(i), bettingProductOptionFormDTO.getOptions_image().get(i)));
        }
        // NOTE: 임시 데이터
        bettingProductAndOptionDTO.setUserId(customUserInfo.getUserId());
        Long productId;
        try {
            productId = productCommandService.save(bettingProductAndOptionDTO);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(Map.of("message",messageSource.getMessage(BettingProductMessageCode.CREATE_FAILED)), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }


    @PostMapping("/api/v1/betting-products/settlement")
    @Operation(summary = "배팅 상품 정산")
    public ResponseEntity<?> settlementBettingProduct(
            @RequestParam Long productId,
            @RequestParam Long optionId,
            @UserId CustomUserInfoDTO customUserInfo,
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

        try{
            bettingProductService.settlementBettingProduct(productId, customUserInfo.getUserId(), optionId);
        }catch (Exception e){
            return new ResponseEntity<>(Map.of("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/betting-products/auto-api")
    @Operation(summary = "관리자용 api 배팅 자동 등록")
    public ResponseEntity<?> saveAutoRegister(
            // @UserId CustomUserInfoDTO customUserInfo,
            // BindingResult bindingResult
    ) {
        // if (bindingResult.hasErrors()) {
        //     Map<String, String> errors = new HashMap<>();
        //     bindingResult.getAllErrors().forEach(error -> {
        //         String fieldName = ((FieldError) error).getField();
        //         String errorMessage = error.getDefaultMessage();
        //         errors.put(fieldName, errorMessage);
        //     });
        //     return ResponseEntity.badRequest().body(errors);
        // }

        try{
            apiBettingProductService.apiTotal(AdminUserIdList.ADMIN_USER_ID, LocalDate.now());
            return ResponseEntity.ok().body(Map.of("message","success"));
        }catch (Exception e){
            return new ResponseEntity<>(Map.of("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/api/v1/betting-products/test/user-prediction-resultdto")
    @Operation(summary = "유저의 예측 결과 조회(테스트)")
    public ResponseEntity<?> getUserPredictionResultDTO(@RequestParam Long productId, @RequestParam Long matchedOptionId){
        return ResponseEntity.ok(bettingProductService.findUserPredictionResult(productId, matchedOptionId));
    }
}
