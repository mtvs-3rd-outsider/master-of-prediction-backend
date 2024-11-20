package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FollowFeedService;
import com.outsider.masterofpredictionbackend.feed.command.application.service.impl.HomeChannelFeedService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class HomeChannelFeedController {

    private final HomeChannelFeedService homeChannelFeedService;
    private final FollowFeedService followFeedService;

    @GetMapping("/home")
    public ResponseEntity<Page<FeedsResponseDTO>> getHomeFeeds(
            @PageableDefault(page = 0, size = 10, sort = "shortAt", direction = Sort.Direction.DESC) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO
    ) {
        // userId가 없는 경우에도 피드 조회 가능
        Page<FeedsResponseDTO> recentFeeds = homeChannelFeedService.getFeeds(pageable, userInfoDTO.getUserId() != null ? userInfoDTO.getUserId()  : 0L);
        return ResponseEntity.ok(recentFeeds);
    }

    @GetMapping("/hot-topic")
    public ResponseEntity<Page<FeedsResponseDTO>> getHotTopicFeeds(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO
    ) {
        // userId가 없는 경우에도 피드 조회 가능
        Pageable viewCountPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "viewCount")
        );

        Page<FeedsResponseDTO> hotTopicFeeds = homeChannelFeedService.getFeeds(viewCountPageable, userInfoDTO.getUserId()  != null ? userInfoDTO.getUserId()  :0L);
        return ResponseEntity.ok(hotTopicFeeds);
    }

    @GetMapping("/like")
    public ResponseEntity<Page<FeedsResponseDTO>> getLikeCountFeeds(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO
    ) {
        // userId가 없는 경우에도 피드 조회 가능
        Pageable likesCountPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "likesCount")
        );

        Page<FeedsResponseDTO> likedFeeds = homeChannelFeedService.getFeeds(likesCountPageable, userInfoDTO.getUserId()  != null ? userInfoDTO.getUserId()  : 0L);
        return ResponseEntity.ok(likedFeeds);
    }

    @GetMapping("/betting")
    public ResponseEntity<List<FeedsResponseDTO>> getFeedsByIds(
            @RequestParam List<Long> ids,
            @UserId CustomUserInfoDTO userInfoDTO
    ) {
        List<FeedsResponseDTO> feedsByIds = homeChannelFeedService.getFeedsByIds(
                ids,
                userInfoDTO.getUserId() != null ? userInfoDTO.getUserId() : 0L
        );
        return ResponseEntity.ok(feedsByIds);
    }

    @GetMapping("/following")
    public ResponseEntity<Page<FeedsResponseDTO>> getFollowingFeeds(
            @PageableDefault(page = 0, size = 10, sort = "shortAt", direction = Sort.Direction.DESC) Pageable pageable,
            @UserId CustomUserInfoDTO userInfoDTO
    ) {
        Page<FeedsResponseDTO> followingFeeds = followFeedService.getFollowingFeeds(
                userInfoDTO.getUserId(),
                pageable
        );
        return ResponseEntity.ok(followingFeeds);
    }

}