package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.impl.MyChannelFeedService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/myfeeds")
public class MyChannelFeedController {
    private final MyChannelFeedService myChannelFeedService;


    public MyChannelFeedController(MyChannelFeedService myChannelFeedService) {
        this.myChannelFeedService=myChannelFeedService;
    }
    @GetMapping("/{channelType}/{channelId}")
    public ResponseEntity<Page<FeedsResponseDTO>> getChannelFeeds(
            @PathVariable String channelType,
            @PathVariable Long channelId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ChannelType type = ChannelType.valueOf(channelType.toUpperCase());
        Page<FeedsResponseDTO> channelFeeds = myChannelFeedService.getFeeds(type, channelId, pageable);
        return ResponseEntity.ok(channelFeeds);
    }
}
