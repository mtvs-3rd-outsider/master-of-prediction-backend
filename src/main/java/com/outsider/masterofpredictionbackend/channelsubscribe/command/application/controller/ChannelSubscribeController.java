package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.controller;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service.ChannelSubscribeService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelSubscribeController {

    private final ChannelSubscribeService channelSubscribeService;

    @Autowired
    public ChannelSubscribeController(ChannelSubscribeService channelSubscribeService) {
        this.channelSubscribeService = channelSubscribeService;
    }

    // 구독 관리 API: subscribe, unsubscribe, renew 모두 처리
    @PostMapping("/subscription")
    public void manageSubscription(@RequestBody ChannelSubscribeRequestDTO dto, @UserId CustomUserInfoDTO userInfoDTO) {

        dto.setUserId(userInfoDTO.getUserId());
        channelSubscribeService.manageSubscription(dto);
    }
}
