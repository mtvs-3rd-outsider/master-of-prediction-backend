package com.outsider.masterofpredictionbackend.channelsubscribe.query.controller;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.service.ChannelSubscribeQueryService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
public class ChannelSubscribeQueryController {

    private final ChannelSubscribeQueryService channelSubscribeQueryService;

    public ChannelSubscribeQueryController(ChannelSubscribeQueryService channelSubscribeQueryService) {
        this.channelSubscribeQueryService = channelSubscribeQueryService;
    }

    // 구독 상태 조회 API
    @GetMapping()
    public boolean isSubscribed(
            @UserId CustomUserInfoDTO userInfoDTO,
            @RequestParam Long channelId,
            @RequestParam Boolean isUserChannel
            ) {return channelSubscribeQueryService.isSubscribed(userInfoDTO.getUserId(), channelId,isUserChannel);
    }
}
