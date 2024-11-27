package com.outsider.masterofpredictionbackend.notification.command.application.controller;


import com.outsider.masterofpredictionbackend.notification.command.application.dto.UpdateIsReadRequestDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.service.NotificationBettingService;
import com.outsider.masterofpredictionbackend.notification.command.application.service.NotificationDMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationCommandController {

    private final NotificationBettingService notificationService;
    private final NotificationDMService notificationDMService;



    // 알림 읽음 상태 업데이트
    @PutMapping("/result/{id}")
    public ResponseEntity<Void> updateIsRead(@PathVariable Long id, @RequestBody UpdateIsReadRequestDTO request) {
        notificationService.updateNotificationIsRead(id, request.getIsRead());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/message/{id}")
    public ResponseEntity<Void> updateDMIsRead(@PathVariable Long id, @RequestBody UpdateIsReadRequestDTO request) {
        notificationDMService.updateNotificationIsRead(id,request.getIsRead());
        return ResponseEntity.ok().build();
    }
}
