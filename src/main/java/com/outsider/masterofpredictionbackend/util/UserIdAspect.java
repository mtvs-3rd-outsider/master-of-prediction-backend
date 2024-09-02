package com.outsider.masterofpredictionbackend.util;

import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class UserIdAspect {

    @Around("execution(* com.outsider.masterofpredictionbackend..*Controller.*(.., @UserId (*), ..)) || execution(* com.outsider.masterofpredictionbackend..*Service.*(.., @UserId (*), ..))")
    public Object injectUserId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // HTTP 헤더에서 사용자 정보 가져오기
        String userIdHeader = request.getHeader("X-User-Id");
        String roleStr = request.getHeader("X-User-Role");
        String email = request.getHeader("X-User-Email");
        String userName = request.getHeader("X-User-Name");

        Long userId = userIdHeader != null ? Long.parseLong(userIdHeader) : 0L;
        Authority role = roleStr != null ? Authority.valueOf(roleStr.toUpperCase()) : Authority.ROLE_USER; // 기본 값 USER로 설정

        // 메서드와 매개변수 정보 가져오기
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = proceedingJoinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        // 매개변수의 어노테이션 확인 후 userId 주입
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof UserId) {
                    if (args[i] instanceof CustomUserInfoDTO) {  // 타입 검증
                        CustomUserInfoDTO customUserInfoDTO = (CustomUserInfoDTO) args[i];
                        customUserInfoDTO.setUserId(userId);
                        customUserInfoDTO.setRole(role);
                        customUserInfoDTO.setEmail(email);
                        customUserInfoDTO.setUsername(userName);
                    } else {
                        throw new IllegalArgumentException("Expected CustomUserInfoDTO, but found " + args[i].getClass().getName());
                    }
                }
            }
        }

        // 수정된 인자들로 메서드 실행
        return proceedingJoinPoint.proceed(args);
    }
}
