package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedLikeController {
    private final FeedLikeService feedLikeService;

    @Autowired
    public FeedLikeController(FeedLikeService feedLikeService){
        this.feedLikeService = feedLikeService;
    }

    // Feed 좋아요 엔드포인트
    @PostMapping("/{feedId}/{userId}")
    public ResponseEntity<Boolean> likeFeed(@PathVariable long feedId, @PathVariable String userId){
        boolean isLike = false;
        try {
            isLike=feedLikeService.toggleLike(feedId, userId);
            return ResponseEntity.ok(isLike);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(isLike);
        }
    }
}
