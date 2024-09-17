package com.outsider.masterofpredictionbackend.betting.query.controller;


import com.outsider.masterofpredictionbackend.betting.query.service.BettingProductQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BettingProductQueryController {

    private final BettingProductQueryService bettingProductQueryService;

    public BettingProductQueryController(BettingProductQueryService bettingProductQueryService) {
        this.bettingProductQueryService = bettingProductQueryService;
    }

    @GetMapping("/bettings")
    public ResponseEntity<?> getBettingProducts(){
        return ResponseEntity.ok(bettingProductQueryService.all());
    }


}
