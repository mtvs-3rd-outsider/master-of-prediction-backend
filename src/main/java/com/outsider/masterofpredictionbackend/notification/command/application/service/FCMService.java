package com.outsider.masterofpredictionbackend.notification.command.application.service;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService  {
    FCMTokenService fcmTokenService;

    public FCMService(FCMTokenService fcmTokenService) {
        this.fcmTokenService = fcmTokenService;
    }

    public void sendNotificationToUser(String userId, String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("REMOVE: " +userId +" " + token);
            fcmTokenService.deleteToken(userId,token);
        }
    }
    public void sendNotification( String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Exception when sending to token: " + token);
            e.printStackTrace();
        }
    }
}
