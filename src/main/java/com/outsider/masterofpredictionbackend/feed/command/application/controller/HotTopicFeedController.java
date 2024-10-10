package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedReadFacadeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class HotTopicFeedController {

    private final FeedReadFacadeService feedReadFacadeService;

    @Autowired
    public HotTopicFeedController(FeedReadFacadeService feedReadFacadeService) {
        this.feedReadFacadeService = feedReadFacadeService;
    }

    @GetMapping("/hot-topic")
    public ResponseEntity<List<FeedsResponseDTO>> getInitialHotTopicFeeds(
            @RequestParam(defaultValue = "10") int size) {
        HotTopicParams params = new HotTopicParams();
        List<FeedsResponseDTO> hotTopicFeeds = feedReadFacadeService.getInitialFeeds(ChannelType.HOT_TOPIC, params, size);
        return ResponseEntity.ok(hotTopicFeeds);
    }

    @GetMapping("/hot-topic/next")
    public ResponseEntity<List<FeedsResponseDTO>> getNextHotTopicFeeds(
            @RequestParam Long lastId,
            @RequestParam(defaultValue = "10") int size) {
        HotTopicParams params = new HotTopicParams();
        List<FeedsResponseDTO> hotTopicFeeds = feedReadFacadeService.getNextFeeds(ChannelType.HOT_TOPIC, params, lastId, size);
        return ResponseEntity.ok(hotTopicFeeds);
    }
}