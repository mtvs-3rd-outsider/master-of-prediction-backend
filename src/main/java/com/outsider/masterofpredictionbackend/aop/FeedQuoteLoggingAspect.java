package com.outsider.masterofpredictionbackend.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Component
public class FeedQuoteLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(FeedQuoteLoggingAspect.class);

    @Before("execution(* com.outsider.masterofpredictionbackend.feed.command.application.service.FeedQuoteService.quoteFeed(..)) && args(originalFeedId, feedCreateDTO, userId, files, youtubeUrls)")
    public void logQuoteFeedInteraction(Long originalFeedId, Object feedCreateDTO, Long userId, List<?> files, List<String> youtubeUrls) {
        // 현재 시간 (타임스탬프)
        String timestamp = LocalDateTime.now().toString();

        // JSON 형식으로 로그 출력
        logger.info("{{\"userId\": {}, \"feedId\": {}, \"actionType\": \"quoteFeed\", \"timestamp\": \"{}\"}}",
                userId,
                originalFeedId,
                timestamp);
    }
}
