package com.outsider.masterofpredictionbackend.aop;

import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LikeCountLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LikeCountLoggingAspect.class);

    // LikeService의 isLike 메서드에 AOP 적용
    @Before("execution(* com.outsider.masterofpredictionbackend.like.command.application.service.LikeService.isLike(..)) && args(likeDTO)")
    public void logLikeInteraction(JoinPoint joinPoint, LikeDTO likeDTO) {
        // 현재 시간
        String timestamp = LocalDateTime.now().toString();

        // JSON 형식으로 로그 출력
        logger.info("{{\"userId\": {}, \"feedId\": {}, \"likeType\": \"{}\", \"viewType\": \"{}\", \"actionType\": \"like\", \"timestamp\": \"{}\"}}",
                likeDTO.getUserId(),
                likeDTO.getTargetId(),
                likeDTO.getLikeType(),
                likeDTO.getViewType(),
                timestamp);
    }
}
