package com.outsider.masterofpredictionbackend.categorychannel.query;

import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category-channels")
public class CategoryChannelQueryController {

    private final CategoryChannelQueryService categoryChannelQueryService;

    @Autowired
    public CategoryChannelQueryController(CategoryChannelQueryService categoryChannelQueryService) {
        this.categoryChannelQueryService = categoryChannelQueryService;
    }

    // 모든 카테고리 채널 조회 (DTO로 변환 후 반환)
    @GetMapping
    public ResponseEntity<List<CategoryChannelDTO>> getAllCategoryChannels() {
        List<CategoryChannelDTO> channels = categoryChannelQueryService.getAllCategoryChannels();
        return ResponseEntity.ok(channels);
    }

    // ID로 특정 카테고리 채널 조회 (DTO로 변환 후 반환)
    @GetMapping("/{channelId}")
    public ResponseEntity<CategoryChannelDTO> getCategoryChannelById(@PathVariable Long channelId) {
        Optional<CategoryChannelDTO> categoryChannel = categoryChannelQueryService.getCategoryChannelById(channelId);
        return categoryChannel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 특정 사용자가 해당 카테고리 채널의 소유자인지 확인
    @GetMapping("/{channelId}/ownership")
    public ResponseEntity<Boolean> isCategoryChannelOwner(
            @PathVariable Long channelId,
            @UserId CustomUserInfoDTO userInfoDTO) {
        boolean isOwner = categoryChannelQueryService.isCategoryChannelOwner(channelId, userInfoDTO.getUserId());
        return ResponseEntity.ok(isOwner);
    }
}

