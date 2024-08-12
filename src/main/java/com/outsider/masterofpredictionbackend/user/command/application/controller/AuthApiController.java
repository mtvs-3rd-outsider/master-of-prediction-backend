package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailAuthDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.LoginRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserInfoRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.RegistUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final AuthService authService;
    private final RegistUserService registUserService;

    @PostMapping("login")
    public ResponseEntity<String> getMemberProfile(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        String token = this.authService.login(request);
        return ResponseEntity.ok().body(token);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDTO user ) {
        registUserService.registUser(user);
        return ResponseEntity.ok().build();
    }

}
