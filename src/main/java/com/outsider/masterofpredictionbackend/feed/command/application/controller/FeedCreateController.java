package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feeds")
public class FeedCreateController {
    private final FeedFacadeService feedFacadeService;

    public FeedCreateController(FeedFacadeService feedFacadeService) {
        this.feedFacadeService = feedFacadeService;
    }

    // Feed 생성 엔드포인트
    @PostMapping
    public ResponseEntity<ResponseMessage> createFeed(@RequestBody FeedCreateDTO feedCreateDTO) {
        try {
            feedFacadeService.createFeed(feedCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("피드가 성공적으로 생성되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 생성에 실패했습니다: " + e.getMessage()));
        }
    }
}
