package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.impl.HotTopicChannelFeedService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.HotTopicParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class HotTopicChannelFeedController {

    private final HotTopicChannelFeedService hotTopicChannelFeedService;

    @Autowired
    public HotTopicChannelFeedController(HotTopicChannelFeedService hotTopicChannelFeedService) {
        this.hotTopicChannelFeedService = hotTopicChannelFeedService;
    }

    @GetMapping("/hot-topic")
    public ResponseEntity<Page<FeedsResponseDTO>> getHottopicFeeds(
            @PageableDefault(page = 0, size = 10, sort = "viewCount", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FeedsResponseDTO> hotTopicFeeds = hotTopicChannelFeedService.getFeeds(pageable);
        return ResponseEntity.ok(hotTopicFeeds);
    }
}