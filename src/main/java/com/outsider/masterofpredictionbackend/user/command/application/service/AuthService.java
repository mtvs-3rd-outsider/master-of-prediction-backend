package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailAuthDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.LoginRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {


    private final JwtUtil jwtUtil;
    private final UserCommandRepository userCommandRepository;
    private final BCryptPasswordEncoder encoder;


    public AuthService(JwtUtil jwtUtil, UserCommandRepository userCommandRepository, BCryptPasswordEncoder encoder, RedisTemplate<String, Object> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.userCommandRepository = userCommandRepository;
        this.encoder = encoder;
        this.redisTemplate = redisTemplate;
    }
    @Transactional
    public String adminLogin(LoginRequestDTO dto) throws AccessDeniedException {
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 이메일로 사용자 조회
        Optional<User> userOptional = userCommandRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        User user = userOptional.get();

        // 탈퇴 상태 확인
        checkWithdrawalStatus(user);

        // 비밀번호 확인
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 권한 확인 (ROLE_ADMIN)
        if (!user.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new AccessDeniedException("관리자 권한이 없습니다.");
        }

        // 토큰 생성
        CustomUserInfoDTO info = new CustomUserInfoDTO(user);
        String accessToken = jwtUtil.createAccessToken(info);
        return accessToken;
    }

    @Transactional
    public String login(LoginRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Optional<User> user = userCommandRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }else
        {
            checkWithdrawalStatus(user.get());
        }
        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(password, user.get().getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfoDTO info = new CustomUserInfoDTO(user.get()) ;

        String accessToken = jwtUtil.createAccessToken(info);
        return accessToken;
    }

    private RedisTemplate<String, Object> redisTemplate;

    public EmailAuthDTO getEmailAuth(String email) {
        ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
        return (EmailAuthDTO) valOperations.get(email);
    }
    public void checkWithdrawalStatus(User user) {
        if (user.getWithdrawal()) {
            user.setWithdrawal(false);
            userCommandRepository.save(user); // 변경사항을 저장
        }
    }
    @Transactional
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        User user = userCommandRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!encoder.matches(currentPassword, user.getPassword())) {
            return false; // 현재 비밀번호가 일치하지 않음
        }

        user.setPassword(encoder.encode(newPassword));
        userCommandRepository.save(user);
        return true;
    }
    @Transactional
    public boolean deleteAccount(String email) {
        User user = userCommandRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 소프트 삭제 처리: isWithdrawal을 true로 설정
        user.setWithdrawal(true);
        userCommandRepository.save(user); // 변경사항 저장

        return true;
    }

}
