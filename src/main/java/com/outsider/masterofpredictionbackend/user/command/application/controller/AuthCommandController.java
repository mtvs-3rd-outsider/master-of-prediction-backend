package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.user.command.application.dto.ChangePasswordRequest;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailAuthDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.AuthService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthCommandController {

    private final UserRegistService registUserService;
    private AuthService authService;
    public AuthCommandController(UserRegistService registUserService, AuthService authService) {
        this.registUserService = registUserService;
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDTO user ) {
        EmailAuthDTO emailAuthDto = authService.getEmailAuth(user.getEmail());
        if (emailAuthDto == null || !emailAuthDto.getFlag()) {
            return ResponseEntity.badRequest().body("Invalid email address");
        }
       Long userId =registUserService.registUser(user);
        return ResponseEntity.ok().body(userId.toString());
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            @UserId CustomUserInfoDTO customUserInfoDTO) {

        boolean success = authService.changePassword(customUserInfoDTO.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        return success ? ResponseEntity.ok("Password changed successfully") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password incorrect or failed to update password");
    }
    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@UserId CustomUserInfoDTO customUserInfoDTOn) {
        boolean success = authService.deleteAccount(customUserInfoDTOn.getEmail());
        return success ? ResponseEntity.ok("Account deleted") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account deletion failed");
    }
}
