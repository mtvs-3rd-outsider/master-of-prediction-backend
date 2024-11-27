package com.outsider.masterofpredictionbackend.categorychannel.command.application.controller;


import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelManagerAssignRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelManagerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category-channels")
@RequiredArgsConstructor
public class CategoryChannelManagerController {

    private final CategoryChannelManagerService categoryChannelManagerService;

    @PostMapping
    @RequestMapping("/{channelId}/managers")
    public ResponseEntity<String> assignManager(
            @PathVariable Long channelId,
            @RequestBody CategoryChannelManagerAssignRequestDTO requestDTO
    ) {
        requestDTO.setCategoryChannelId(channelId);
        categoryChannelManagerService.assignManager(requestDTO);
        return ResponseEntity.ok().body("카테고리 채널 매니저 임명이 완료되었습니다.");
    }
}


