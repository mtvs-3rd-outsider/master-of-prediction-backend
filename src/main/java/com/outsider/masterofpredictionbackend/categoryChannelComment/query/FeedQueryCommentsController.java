package com.outsider.masterofpredictionbackend.categoryChannelComment.query;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedQueryCommentsController {

    private final FeedQueryCommentsService feedQueryCommentsService;

    public FeedQueryCommentsController(FeedQueryCommentsService feedQueryCommentsService) {
        this.feedQueryCommentsService = feedQueryCommentsService;
    }

    @GetMapping("/api/v1/feed-products/comments")
    @Operation(summary = "feed의 댓글 조회")
    public ResponseEntity<List<FeedQueryCommentsDTO>> getBettingChannelComments(@RequestParam Long feedId){
        return ResponseEntity.ok(feedQueryCommentsService.findBettingChannelComments(feedId));
    }
}
