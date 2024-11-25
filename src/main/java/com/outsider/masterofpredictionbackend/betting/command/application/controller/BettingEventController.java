package com.outsider.masterofpredictionbackend.betting.command.application.controller;

import com.outsider.masterofpredictionbackend.betting.command.application.service.BettingEventService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/betting-products")
public class BettingEventController {

    private final BettingEventService bettingEventService;

    public BettingEventController(BettingEventService bettingEventService) {
        this.bettingEventService = bettingEventService;
    }

    /**
     * 클라이언트가 특정 상품(room)에 연결
     */
    @GetMapping(value = "/connect/{productId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectToProductRoom(@PathVariable Long productId) {
        return bettingEventService.connectToProductRoom(productId);
    }

}
