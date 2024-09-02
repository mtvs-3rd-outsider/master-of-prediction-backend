package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // JWT 토큰이 헤더에 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // JWT 토큰의 유효성 검증
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserId(token);
                String email = jwtUtil.getUserEmail(token);
                String userName = jwtUtil.getUserName(token);
                String role = jwtUtil.getRole(token);

                // CustomUserDetail 객체 생성
                UserDetails userDetails = new CustomUserDetail(userId, email, userName, role);

                // UsernamePasswordAuthenticationToken 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // SecurityContextHolder에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 사용자 정보를 HTTP 헤더에 추가하여 다른 마이크로서비스로 전달
                response.setHeader("X-User-Id", userId.toString());
                response.setHeader("X-User-Email", email);
                response.setHeader("X-User-Name", userName);
                response.setHeader("X-User-Role", role);
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘기기
    }
}
