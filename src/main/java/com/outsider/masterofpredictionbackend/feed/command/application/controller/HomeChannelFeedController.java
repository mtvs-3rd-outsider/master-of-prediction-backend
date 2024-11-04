package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.impl.HomeChannelFeedService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feeds")
public class HomeChannelFeedController {

    private final HomeChannelFeedService homeChannelFeedService;

    @Autowired
    public HomeChannelFeedController(HomeChannelFeedService homeChannelFeedService) {
        this.homeChannelFeedService = homeChannelFeedService;
    }

    @GetMapping("/hot-topic")
    public ResponseEntity<Page<FeedsResponseDTO>> getHottopicFeeds(
            @PageableDefault(page = 0, size = 10, sort = "viewCount", direction = Sort.Direction.DESC) Pageable pageable,
            @UserId CustomUserInfoDTO customUserInfoDTO) {
        long userId;
        if(customUserInfoDTO.getUserId()==null){
            userId = -1L;
        }else {
            userId = customUserInfoDTO.getUserId();
        }

        Page<FeedsResponseDTO> hotTopicFeeds = homeChannelFeedService.getFeeds(pageable,userId);
        return ResponseEntity.ok(hotTopicFeeds);
    }

    @GetMapping("/home")
    public ResponseEntity<Page<FeedsResponseDTO>> getCreateAtFeeds(
            @PageableDefault(page = 0, size = 10, sort = "shortAt", direction = Sort.Direction.DESC) Pageable pageable,
            @UserId CustomUserInfoDTO customUserInfoDTO) {
        long userId;
        if(customUserInfoDTO.getUserId()==null){
            userId = -1L;
        }else {
            userId = customUserInfoDTO.getUserId();
        }


        Page<FeedsResponseDTO> hotTopicFeeds = homeChannelFeedService.getFeeds(pageable,userId);
        return ResponseEntity.ok(hotTopicFeeds);
    }

    @GetMapping("/like")
    public ResponseEntity<Page<FeedsResponseDTO>> getLikeCountFeeds(
            @PageableDefault(page = 0, size = 10, sort = "likesCount", direction = Sort.Direction.DESC) Pageable pageable,
            @UserId CustomUserInfoDTO customUserInfoDTO) {
        long userId;
        if(customUserInfoDTO.getUserId()==null){
            userId = -1L;
        }else {
            userId = customUserInfoDTO.getUserId();
        }

        Page<FeedsResponseDTO> hotTopicFeeds = homeChannelFeedService.getFeeds(pageable,userId);
        return ResponseEntity.ok(hotTopicFeeds);
    }
}