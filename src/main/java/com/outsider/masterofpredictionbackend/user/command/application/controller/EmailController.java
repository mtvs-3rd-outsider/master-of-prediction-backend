package com.outsider.masterofpredictionbackend.user.command.application.controller;



import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailCheckDto;
import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailRequestDto;
import com.outsider.masterofpredictionbackend.user.command.application.service.EmailSendService;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Profile("prod")
@RequestMapping("/api/v1/auth")
public class EmailController {
    private final EmailSendService emailSendService;
    private  final UserCommandRepository userManagementService;

    /* Send Email: 인증번호 전송 버튼 click */
    @PostMapping("/signup/email")
    public Map<String, Object> mailSend(@RequestBody @Valid EmailRequestDto emailRequestDto) {


        Map<String, Object> response = new HashMap<>();
        if(userManagementService.findByEmail(emailRequestDto.getEmail()).isPresent()) {
            response.put("message", "이미 가입된 이메일 입니다.");
            response.put("code", 400);
        }else{
            boolean isValid = emailSendService.joinEmail(emailRequestDto.getEmail());
            if(isValid)
            {
                response.put("message", "인증 코드를 전송하였습니다.");
                response.put("code", 200);

            }else
            {
                response.put("message", "유효하지 않은 이메일 입니다.");
                response.put("code", 400);

            }
        }
        return response;
    }

    /* Email Auth: 인증번호 입력 후 인증 버튼 click */
    @PostMapping("/signup/emailAuth")
    public String authCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = emailSendService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        if (checked) {
            return "이메일 인증 성공!";
        }
        else {
            throw new NullPointerException("이메일 인증 실패!");
        }
    }
}