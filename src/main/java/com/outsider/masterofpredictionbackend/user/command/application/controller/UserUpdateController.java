package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.file.MinioService;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UserChannelUpdateDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.MyChannelInfoUpdateService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserProfileUpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/v1/users")
public class UserUpdateController {

    private final UserProfileUpdateService userUpdateService;

    @Autowired
    public UserUpdateController(UserProfileUpdateService userUpdateService) {
        this.userUpdateService = userUpdateService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateProfile(
            @PathVariable("userId") Long userId,
            @ModelAttribute UserChannelUpdateDTO updateDTO,
            @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage,
            @RequestPart(value = "avatarImage", required = false) MultipartFile avatarImage
    ) throws Exception {

        userUpdateService.updateUser(updateDTO, userId, bannerImage, avatarImage);

        return ResponseEntity.ok("Profile updated successfully!");
    }
}
