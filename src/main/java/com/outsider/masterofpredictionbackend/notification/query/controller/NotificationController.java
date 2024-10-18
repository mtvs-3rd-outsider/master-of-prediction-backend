package com.outsider.masterofpredictionbackend.notification.query.controller;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.query.NotificationQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationQueryService notificationService;
    public NotificationController(NotificationQueryService notificationService) {
        this.notificationService = notificationService;
    }
    @GetMapping
    public ResponseEntity<Page<Notification>> getUserNotifications(
            @RequestParam("userId") String userId, Pageable pageable) {
        Page<Notification> notifications = notificationService.getNotificationsByUserId(userId, pageable);
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }
}
