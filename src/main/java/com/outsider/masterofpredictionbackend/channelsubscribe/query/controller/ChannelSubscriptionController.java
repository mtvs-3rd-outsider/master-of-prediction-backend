package com.outsider.masterofpredictionbackend.channelsubscribe.query.controller;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.ChannelInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.UserInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.service.ChannelSubscriptionService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class ChannelSubscriptionController {

    private final ChannelSubscriptionService subscriptionService;

    public ChannelSubscriptionController(ChannelSubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // 사용자가 팔로우한 모든 채널 목록을 페이징하여 가져오기
    @GetMapping("/user/{userId}/following")
    public ResponseEntity<Page<ChannelInfo>> getFollowingByUserId(
            @PathVariable Long userId,
            @RequestParam boolean isUserChannel,  // 컨트롤러에서 isUserChannel 값을 받음
            @RequestParam(required = false, defaultValue = "ALL") String flag,  // 추가된 flag 파라미터
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO) {
        Page<ChannelInfo> following = subscriptionService.getAllFollowingByUserId(userId, userInfoDTO.getUserId(), isUserChannel, pageable, flag);
        return ResponseEntity.ok(following);
    }

    // 특정 채널의 구독자 목록을 페이징하여 가져오기
    @GetMapping("/channel/{channelId}/subscribers")
    public ResponseEntity<Page<UserInfo>> getSubscribersByChannelId(
            @PathVariable Long channelId,
            @RequestParam boolean isUserChannel,  // 컨트롤러에서 isUserChannel 값을 받음
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO) {
        Page<UserInfo> subscribers = subscriptionService.getAllSubscribersByChannelId(channelId, userInfoDTO.getUserId(), isUserChannel, pageable);
        return ResponseEntity.ok(subscribers);
    }

    // 사용자가 팔로우한 채널 수 가져오기
    @GetMapping("/user/{userId}/following/count")
    public ResponseEntity<Long> getFollowingCountByUserId(
            @PathVariable Long userId,
            @RequestParam boolean isUserChannel,  // 컨트롤러에서 isUserChannel 값을 받음
            @UserId CustomUserInfoDTO userInfoDTO) {
        Long followingCount = subscriptionService.getFollowingCountByUserId(userId,  isUserChannel);
        return ResponseEntity.ok(followingCount);
    }

    // 특정 채널의 구독자 수 가져오기
    @GetMapping("/channel/{channelId}/subscribers/count")
    public ResponseEntity<Long> getSubscribersCountByChannelId(
            @PathVariable Long channelId,
            @RequestParam boolean isUserChannel,  // 컨트롤러에서 isUserChannel 값을 받음
            @UserId CustomUserInfoDTO userInfoDTO) {
        Long subscriberCount = subscriptionService.getSubscribersCountByChannelId(channelId, isUserChannel);
        return ResponseEntity.ok(subscriberCount);
    }
}
