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

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class HomeChannelFeedController {

    private final HomeChannelFeedService homeChannelFeedService;
    private final FollowFeedService followFeedService;

    @GetMapping("/hot-topic")
    public ResponseEntity<Page<FeedsResponseDTO>> getHottopicFeeds(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @UserId CustomUserInfoDTO customUserInfoDTO) {
        long userId = customUserInfoDTO.getUserId() != null ? customUserInfoDTO.getUserId() : -1L;

        // 조회수 기준 정렬
        Pageable viewCountPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "viewCount")
        );

        Page<FeedsResponseDTO> hotTopicFeeds = homeChannelFeedService.getFeeds(viewCountPageable, userId);
        return ResponseEntity.ok(hotTopicFeeds);
    }

    @GetMapping("/home")
    public ResponseEntity<Page<FeedsResponseDTO>> getCreateAtFeeds(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @UserId CustomUserInfoDTO customUserInfoDTO) {
        long userId = customUserInfoDTO.getUserId() != null ? customUserInfoDTO.getUserId() : -1L;

        // 최신순 정렬
        Pageable shortAtPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "shortAt")
        );

        Page<FeedsResponseDTO> recentFeeds = homeChannelFeedService.getFeeds(shortAtPageable, userId);
        return ResponseEntity.ok(recentFeeds);
    }

    @GetMapping("/like")
    public ResponseEntity<Page<FeedsResponseDTO>> getLikeCountFeeds(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @UserId CustomUserInfoDTO customUserInfoDTO) {
        long userId = customUserInfoDTO.getUserId() != null ? customUserInfoDTO.getUserId() : -1L;

        // 좋아요순 정렬
        Pageable likesCountPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "likesCount")
        );

        Page<FeedsResponseDTO> likedFeeds = homeChannelFeedService.getFeeds(likesCountPageable, userId);
        return ResponseEntity.ok(likedFeeds);
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