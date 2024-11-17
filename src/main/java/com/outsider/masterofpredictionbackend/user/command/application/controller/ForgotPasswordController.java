package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.user.command.application.dto.ForgotPasswordRequest;
import com.outsider.masterofpredictionbackend.user.command.application.dto.ResetPasswordRequest;
import com.outsider.masterofpredictionbackend.user.command.application.service.ForgotPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/auth")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            forgotPasswordService.processForgotPassword(request.getEmail());
            return new ResponseEntity<>("비밀번호 재설정 이메일이 전송되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("비밀번호 재설정 이메일 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isReset = forgotPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        if (isReset) {
            return new ResponseEntity<>("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("유효하지 않거나 만료된 토큰입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}

