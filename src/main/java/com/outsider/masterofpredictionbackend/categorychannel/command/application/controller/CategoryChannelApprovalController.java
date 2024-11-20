package com.outsider.masterofpredictionbackend.categorychannel.command.application.controller;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelApprovalService;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/category-channels")
public class CategoryChannelApprovalController {

    private final CategoryChannelApprovalService categoryChannelApprovalService;

    @Autowired
    public CategoryChannelApprovalController(CategoryChannelApprovalService categoryChannelApprovalService) {
        this.categoryChannelApprovalService = categoryChannelApprovalService;
    }

    // 카테고리 채널 상태 변경 (승인 또는 거절)
    @PostMapping("/{channelId}/status")
    public void changeCategoryChannelStatus(
            @PathVariable Long channelId,
            @RequestParam CategoryChannelStatus status) {
        categoryChannelApprovalService.changeCategoryChannelStatus(channelId, status);
    }
}
