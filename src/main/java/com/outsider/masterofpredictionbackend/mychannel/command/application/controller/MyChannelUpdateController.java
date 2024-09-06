package com.outsider.masterofpredictionbackend.mychannel.command.application.controller;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.ChannelDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/my-channel")
public class MyChannelUpdateController {

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateProfile(
            @PathVariable("userId") String userId,
            @ModelAttribute ChannelDTO channelDto,  // 채널 관련 데이터
            @ModelAttribute UserDTO userDto,        // 사용자 관련 데이터
            @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage,  // 이미지 파일 (필수 아님)
            @RequestPart(value = "avatarImage", required = false) MultipartFile avatarImage   // 이미지 파일 (필수 아님)
    ) {
        // 파일 처리
        if (bannerImage != null && !bannerImage.isEmpty()) {
            // bannerImage 처리 로직
        }

        if (avatarImage != null && !avatarImage.isEmpty()) {
            // avatarImage 처리 로직
        }

        // DTO로 받은 데이터 처리


        // DB 업데이트 로직 또는 다른 처리를 여기에 추가

        return ResponseEntity.ok("Profile updated successfully!");
    }
}