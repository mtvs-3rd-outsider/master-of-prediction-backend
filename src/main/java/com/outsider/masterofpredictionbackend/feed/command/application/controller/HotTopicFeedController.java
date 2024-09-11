package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.HotTopicFeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.HotTopicFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class HotTopicFeedController {

    private final HotTopicFeedService hotTopicFeedService;

    @Autowired
    public HotTopicFeedController(HotTopicFeedService hotTopicFeedService) {
        this.hotTopicFeedService = hotTopicFeedService;
    }

    @GetMapping("/hot-topic")
    public ResponseEntity<List<HotTopicFeedResponseDTO>> getInitialHotTopicFeeds(
            @RequestParam(defaultValue = "10") int size) {
        List<HotTopicFeedResponseDTO> hotTopicFeeds = hotTopicFeedService.getInitialHotTopicFeeds(size);
        return ResponseEntity.ok(hotTopicFeeds);
    }

    @GetMapping("/hot-topic/next")
    public ResponseEntity<List<HotTopicFeedResponseDTO>> getNextHotTopicFeeds(
            @RequestParam Long lastId,
            @RequestParam(defaultValue = "10") int size) {
        List<HotTopicFeedResponseDTO> hotTopicFeeds = hotTopicFeedService.getNextHotTopicFeeds(lastId, size);
        return ResponseEntity.ok(hotTopicFeeds);
    }
}