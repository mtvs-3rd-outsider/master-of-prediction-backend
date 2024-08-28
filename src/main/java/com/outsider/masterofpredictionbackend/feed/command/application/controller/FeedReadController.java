package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/feeds")
public class FeedReadController {
    private final FeedFacadeService feedFacadeService;

    public FeedReadController(FeedFacadeService feedFacadeService) {
        this.feedFacadeService = feedFacadeService;
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<Map<String, Object>> getFeed(@PathVariable Long feedId) {
        Map<String, Object> response = new HashMap<>();

        try {
            FeedResponseDTO feed = feedFacadeService.getFeed(feedId);
            response.put("success", true);
            response.put("message", "피드를 성공적으로 가져왔습니다.");
            response.put("data", feed);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "피드를 가져오는 데 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}