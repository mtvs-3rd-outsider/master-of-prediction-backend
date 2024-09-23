package com.outsider.masterofpredictionbackend.bettingChannelComment.command.query;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BettingQueryCommentsController {

    private final BettingQueryCommentsService bettingQueryCommentsService;

    public BettingQueryCommentsController(BettingQueryCommentsService bettingQueryCommentsService) {
        this.bettingQueryCommentsService = bettingQueryCommentsService;
    }

    @GetMapping("/api/v1/betting-products/comments")
    @Operation(summary = "배팅 채널의 댓글 조회")
    public ResponseEntity<List<BettingQueryCommentsDTO>> getBettingChannelComments(@RequestParam Long bettingId){
        return ResponseEntity.ok(bettingQueryCommentsService.findBettingChannelComments(bettingId));
    }
}
