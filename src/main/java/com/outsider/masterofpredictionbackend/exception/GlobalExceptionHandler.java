package com.outsider.masterofpredictionbackend.exception;

import com.outsider.masterofpredictionbackend.common.exception.CustomExceptionMapping;
import com.outsider.masterofpredictionbackend.notification.DiscordMessage;
import com.outsider.masterofpredictionbackend.notification.DiscordNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Profile("prod")
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final DiscordNotificationService discordService;
    private final MessageSource messageSource;

    // NullPointerException 처
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException ex, WebRequest request) {
        // 예외 로그 출력이나 추가 처리 가능
        Map<String, String> response = new HashMap<>();
        response.put("message", "요청 데이터가 null입니다.");
        response.put("details", ex.getMessage());  // 예외 메시지 추가
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        errors.put("message", "유효성 검사에 실패했습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    // 404 오류 처리 (Discord 알림 없이)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NoHandlerFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "요청한 리소스를 찾을 수 없습니다.");
        response.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex, WebRequest request) {
        CustomExceptionMapping mapping = CustomExceptionMapping.fromException(ex.getClass());
        Map<String, String> response = new HashMap<>();

        if (mapping != null) {
            // 다국어 메시지 처리
            response.put("message", mapping.getMessage(messageSource));
            return ResponseEntity.status(mapping.getHttpStatus()).body(response);
        } else {
            // 500 내부 서버 오류의 경우 Discord 알림 전송
            DiscordMessage message = discordService.createMessage(ex, request);
            discordService.sendAlarm(message);

            // 기본 오류 응답
            response.put("message", "서버 오류가 발생했습니다.");
            response.put("details", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
