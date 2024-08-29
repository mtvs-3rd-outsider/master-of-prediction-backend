package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedLikeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds")
public class FeedLikeController {
    private final FeedLikeService feedLikeService;

    @Autowired
    public FeedLikeController(FeedLikeService feedLikeService){
        this.feedLikeService = feedLikeService;
    }

    // Feed 좋아요 엔드포인트
    @PostMapping("/{feedId}/{userId}")
    public ResponseEntity<ResponseMessage> likeFeed(@PathVariable long feedId, @PathVariable String userId){
        try {
            feedLikeService.toggleLike(feedId, userId);
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 수정에 실패했습니다: " + e.getMessage()));
        }
    }
}
