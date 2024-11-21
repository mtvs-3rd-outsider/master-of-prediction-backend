package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.betting.command.application.controller.BettingProductController;
import com.outsider.masterofpredictionbackend.betting.query.controller.BettingProductQueryController;
import com.outsider.masterofpredictionbackend.bettingorder.query.controller.BettingOrderQueryController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        BettingOrderQueryController.class,
        BettingProductController.class,
        BettingProductQueryController.class
})
public class BettingProductExceptionHandler {

    private final CustomBettingProductMessage messageSource;

    public BettingProductExceptionHandler(CustomBettingProductMessage messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BettingProductException.NotFound.class)
    public ResponseEntity<?> handleNotFound(RuntimeException ex) {
        log.info("handleNotFound");
        return new ResponseEntity<>(Map.of("message",
                messageSource.getMessage(BettingProductMessageCode.NOT_FOUND))
                , HttpStatus.NOT_FOUND);
    }
    //
    // @ExceptionHandler(BettingProductException.BadRequest.class)
    // public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
    //     return ResponseEntity.badRequest().body(Map.of("message",
    //             messageSource.getMessage(BettingProductMessageCode.BAD_REQUEST)));
    // }
    
}