package com.outsider.masterofpredictionbackend.user.query.application.controller;

import com.outsider.masterofpredictionbackend.categorychannel.query.CategoryChannelDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.query.application.service.UserInfoService;
import com.outsider.masterofpredictionbackend.user.query.usersearch.UserSearchModel;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserInfoController {

    public UserInfoService userService;
    public UserInfoController(UserInfoService userService) {
        this.userService = userService;
    }



    @GetMapping
    public ResponseEntity<Page<UserInfoResponseDTO>> getAllUsers(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserInfoResponseDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

}
