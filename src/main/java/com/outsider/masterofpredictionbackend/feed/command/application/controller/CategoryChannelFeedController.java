package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.impl.CategoryChannelFeedService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import io.lettuce.core.GeoArgs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categoryfeeds")
public class CategoryChannelFeedController {
    private final CategoryChannelFeedService categoryChannelFeedService;

    public CategoryChannelFeedController(CategoryChannelFeedService categoryChannelFeedService) {
        this.categoryChannelFeedService = categoryChannelFeedService;
    }

    @GetMapping("/{channelType}/{channelId}")
    public ResponseEntity<Page<FeedsResponseDTO>> getChannelFeeds(
            @PathVariable String channelType,
            @PathVariable Long channelId,
            @PageableDefault(page = 0, size = 10, sort = "shortAt", direction = Sort.Direction.DESC) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO
    ) {
        long userId;
        if(userInfoDTO.getUserId()==null){
            userId = 0L;
        }else {
            userId = userInfoDTO.getUserId();
        }
        ChannelType type = ChannelType.valueOf(channelType.toUpperCase());
        Page<FeedsResponseDTO> channelFeeds = categoryChannelFeedService.getFeeds(type, channelId, pageable, userId);
        return ResponseEntity.ok(channelFeeds);
    }
}
