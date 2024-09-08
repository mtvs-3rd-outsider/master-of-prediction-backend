package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.file.MinioService;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UserChannelUpdateDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.MyChannelInfoUpdateService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserProfileUpdateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/v1/users")
public class UserUpdateController {

        public MinioService minioService;
        public MyChannelInfoUpdateService myChannelInfoUpdateService;

    public UserUpdateController(MinioService minioService, MyChannelInfoUpdateService myChannelInfoUpdateService, UserProfileUpdateService userUpdateService) {
        this.minioService = minioService;
        this.myChannelInfoUpdateService = myChannelInfoUpdateService;
        this.userUpdateService = userUpdateService;
    }

    public UserProfileUpdateService userUpdateService;
        @PutMapping("/{userId}")
        public ResponseEntity<String> updateProfile(
                @PathVariable("userId") Long userId,
                @ModelAttribute UserChannelUpdateDTO updateDTO,  // 채널 관련 데이터
                @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage,  // 이미지 파일 (필수 아님)
                @RequestPart(value = "avatarImage", required = false) MultipartFile avatarImage   // 이미지 파일 (필수 아님)
                ) throws Exception {

            // 파일 처리
            if (bannerImage != null && !bannerImage.isEmpty()) {
                // bannerImage 처리 로직
                String bannerImg = minioService.uploadFile(bannerImage);
                updateDTO.setBannerImg(bannerImg);
            }

            if (avatarImage != null && !avatarImage.isEmpty()) {
                // avatarImage 처리 로직
                String userImg = minioService.uploadFile(avatarImage);
                updateDTO.setUserImg(userImg);

            }
            userUpdateService.updateUser(updateDTO,userId);


            // DB 업데이트 로직 또는 다른 처리를 여기에 추가

            return ResponseEntity.ok("Profile updated successfully!");
        }
}
