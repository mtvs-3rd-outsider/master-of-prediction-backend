package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailAuthDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.AuthService;
import com.outsider.masterofpredictionbackend.user.command.application.service.RegistUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthCommandController {

    private final RegistUserService registUserService;
    private AuthService authService;
    public AuthCommandController(RegistUserService registUserService, AuthService authService) {
        this.registUserService = registUserService;
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDTO user ) {
//        EmailAuthDTO emailAuthDto = authService.getEmailAuth(user.getEmail());
//        if (emailAuthDto == null || !emailAuthDto.getFlag()) {
//            return ResponseEntity.badRequest().body("Invalid email address");
//        }
        registUserService.registUser(user);
        return ResponseEntity.ok().build();
    }

}
