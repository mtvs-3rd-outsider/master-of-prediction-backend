package com.outsider.masterofpredictionbackend.categorychannel.command.application.controller;


import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelRegistService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/category-channels")
public class CategoryChannelRegistController {

    private final CategoryChannelRegistService categoryChannelRegistService;

    @Autowired
    public CategoryChannelRegistController(CategoryChannelRegistService categoryChannelRegistService) {
        this.categoryChannelRegistService = categoryChannelRegistService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCategoryChannel(
            @ModelAttribute CategoryChannelRegistRequestDTO registRequestDTO,  // 채널 등록 데이터
            @RequestPart(value = "representativeImage", required = false) MultipartFile representativeImageFile,  // 대표 이미지 파일
            @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImageFile,  // 배너 이미지 파일
            @UserId CustomUserInfoDTO userInfoDTO // 유저 ID
    ) {
        try {
            categoryChannelRegistService.registerCategoryChannel(registRequestDTO, representativeImageFile, bannerImageFile, userInfoDTO.getUserId());
            return ResponseEntity.ok("Category channel registered successfully!");
        } catch (Exception e) {
            // 예외 처리: 실제로는 커스텀 예외 처리 로직을 추가할 수 있습니다.
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while registering category channel.");
        }
    }
}
