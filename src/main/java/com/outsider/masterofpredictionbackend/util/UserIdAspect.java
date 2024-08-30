package com.outsider.masterofpredictionbackend.util;


import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserService;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
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

        // JwtAuthFilter에서 설정된 userId 가져오기
        Long userId = (Long) request.getAttribute("userId");
        String roleStr = (String) request.getAttribute("role");
        Authority role = Authority.valueOf(roleStr.toUpperCase());
        String email = (String) request.getAttribute("email");
        String userName = (String) request.getAttribute("userName");

        if (userId == null) {
            userId= 0L;
        }

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
