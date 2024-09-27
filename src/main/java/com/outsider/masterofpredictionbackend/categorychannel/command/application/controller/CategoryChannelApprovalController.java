package com.outsider.masterofpredictionbackend.categorychannel.command.application.controller;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelApprovalService;
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

    @PostMapping("/approve/{channelId}")
    public void approveCategoryChannel(@PathVariable Long channelId) {
        categoryChannelApprovalService.approveCategoryChannel(channelId);
    }
}
