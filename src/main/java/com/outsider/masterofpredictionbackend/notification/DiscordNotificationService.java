package com.outsider.masterofpredictionbackend.notification;


import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscordNotificationService {

    private final WebClient webClient;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    public void sendAlarm(DiscordMessage message) {
        webClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // 동기적으로 실행
    }

    public DiscordMessage createMessage(Exception e, WebRequest request) {
        String errorMessage = "An error occurred: " + e.getMessage();
        String stackTrace = getStackTrace(e);

        // Stack Trace가 너무 길면 자르기 (Discord 제한사항)
        if (stackTrace.length() > 1000) {
            stackTrace = stackTrace.substring(0, 1000) + "...";
        }

        return DiscordMessage.builder()
                .content("# 🚨 에러 발생")
                .embeds(
                        List.of(
                                Embed.builder()
                                        .title("ℹ️ 에러 정보")
                                        .description(
                                                "### 🕖 발생 시간\n"
                                                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                                        + "\n\n"
                                                        + "### 🔗 요청 URL\n"
                                                        + createRequestFullPath(request)
                                                        + "\n\n"
                                                        + "### 📄 Stack Trace\n"
                                                        + "```\n"
                                                        + stackTrace
                                                        + "\n```"
                                        )
                                        .color(0xFF0000) // 빨간색
                                        .thumbnail(Thumbnail.builder()
                                                .url("https://i.imgur.com/ZXwQfGG.png") // 원하는 이미지 URL
                                                .build())
                                        .footer(Footer.builder()
                                                .text("Error Notification")
                                                .icon_url("https://i.imgur.com/ZXwQfGG.png") // 원하는 푸터 아이콘 URL
                                                .build())
                                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                        .build()
                        )
                )
                .build();
    }

    private String createRequestFullPath(WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        return fullPath;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}


