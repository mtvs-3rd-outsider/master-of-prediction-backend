package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds")
public class FeedDeleteController {
    private final FeedFacadeService feedFacadeService;

    public FeedDeleteController(FeedFacadeService feedFacadeService) {
        this.feedFacadeService = feedFacadeService;
    }

    // Feed 삭제 엔드포인트
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ResponseMessage> deleteFeed(@PathVariable Long feedId) {
        try {
            feedFacadeService.deleteFeed(feedId);
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 삭제되었습니다.", feedId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 삭제에 실패했습니다: " + e.getMessage(), feedId));
        }
    }
}