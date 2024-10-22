package com.outsider.masterofpredictionbackend.notification.command.application.controller;


import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.UpdateIsReadRequestDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationCommandController {

    private final NotificationService notificationService;

    public NotificationCommandController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }



    // 알림 읽음 상태 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateIsRead(@PathVariable Long id, @RequestBody UpdateIsReadRequestDTO request) {
        notificationService.updateNotificationIsRead(id, request.getIsRead());
        return ResponseEntity.ok().build();
    }
}
