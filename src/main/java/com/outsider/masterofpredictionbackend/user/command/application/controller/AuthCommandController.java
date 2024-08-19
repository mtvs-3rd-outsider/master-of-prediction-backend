package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.RegistUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthCommandController {

    private final RegistUserService registUserService;

    public AuthCommandController(RegistUserService registUserService) {
        this.registUserService = registUserService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDTO user ) {
        registUserService.registUser(user);
        return ResponseEntity.ok().build();
    }

}
