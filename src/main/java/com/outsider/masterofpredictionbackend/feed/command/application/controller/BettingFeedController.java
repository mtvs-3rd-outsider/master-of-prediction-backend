package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.BettingFeedService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/betting-feed/")
@RequiredArgsConstructor
public class BettingFeedController {
    private final BettingFeedService bettingFeedService;

    @PostMapping("{id}")
    public ResponseEntity<String> createBettingFeed(@PathVariable Long id){
        try{
            bettingFeedService.createBettingFeed(id);
            return ResponseEntity.ok("성공");
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<String> modifyBettingFeed(@PathVariable Long id){
        try{
            bettingFeedService.createBettingFeed(id);
            return ResponseEntity.ok("성공");
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
