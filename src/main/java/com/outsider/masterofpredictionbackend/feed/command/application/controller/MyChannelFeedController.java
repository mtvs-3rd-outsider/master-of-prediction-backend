package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedReadFacadeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.vo.MyChannelParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class MyChannelFeedController {
    private final FeedReadFacadeService feedReadFacadeService;

    public MyChannelFeedController(FeedReadFacadeService feedReadFacadeService) {
        this.feedReadFacadeService = feedReadFacadeService;
    }
    @GetMapping("/mychannel/{channelId}")
    public ResponseEntity<List<FeedsResponseDTO>> getInitialMyChannelFeeds(
            @RequestParam(defaultValue = "10") int size,
            @PathVariable long channelId) {
        MyChannelParams params = new MyChannelParams(channelId);
        List<FeedsResponseDTO> myChannelFeeds = feedReadFacadeService.getInitialFeeds(ChannelType.MY_CHANNEL, params, size);
        return ResponseEntity.ok(myChannelFeeds);
    }

    @GetMapping("/hot-mychannel/{channelId}/next")
    public ResponseEntity<List<FeedsResponseDTO>> getNextMyChannelFeeds(
            @RequestParam Long lastId,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable long channelId) {
        MyChannelParams params = new MyChannelParams(channelId);
        List<FeedsResponseDTO> myChannelFeeds = feedReadFacadeService.getNextFeeds(ChannelType.MY_CHANNEL, params, lastId, size);
        return ResponseEntity.ok(myChannelFeeds);
    }
}
