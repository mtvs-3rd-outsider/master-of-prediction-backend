package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kotlin.RequiresOptIn;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final JavaMailSender mailSender;
    private final UserCommandRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    @Value("${google.redirect.successfulUri}")
    private String successfulUri;
    @Value("${spring.mail.username}")
    private String serviceName;
    public void processForgotPassword(String email) {
        // 사용자 검증 (유효한 이메일인지 확인)
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) throw new IllegalArgumentException("이메일을 찾을 수 없습니다.");

        // 토큰 생성
        String token = UUID.randomUUID().toString();
        String redisKey = "passwordResetToken:" + token; // token을 사용하도록 수정

        // Redis에 토큰 저장 (예: 15분 유효 기간)
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(redisKey, email, 15, TimeUnit.MINUTES); // token을 키로 하고 email을 값으로 저장

        // 비밀번호 재설정 링크 생성
        String resetLink = successfulUri + "reset-password?token=" + token;

        // 이메일 전송
        sendResetEmail(email, resetLink);
    }
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        String redisKey = "passwordResetToken:" + token;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String email = ops.get(redisKey);

        if (email == null) {
            return false; // 유효하지 않거나 만료된 토큰
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다."));

        // 새 비밀번호 설정
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 토큰 삭제
        redisTemplate.delete(redisKey);
        return true;
    }
    private void sendResetEmail(String email, String resetLink) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom(serviceName); // service name
            helper.setTo(email);
            helper.setSubject("비밀번호 재설정 링크");

            // HTML 형식으로 이메일 내용 작성
            String htmlContent = "<p>비밀번호를 재설정하려면 다음 링크를 클릭하세요:</p>"
                    + "<p><a href=\"" + resetLink + "\">비밀번호 재설정 링크</a></p>";

            helper.setText(htmlContent, true); // HTML 형식 활성화
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

