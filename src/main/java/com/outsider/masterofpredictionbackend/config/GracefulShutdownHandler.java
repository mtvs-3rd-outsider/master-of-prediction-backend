package com.outsider.masterofpredictionbackend.config;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class GracefulShutdownHandler {

    @PreDestroy
    public void onShutdown() {
        System.out.println("Application is shutting down. Waiting for Kafka events to be processed...");
        try {
            Thread.sleep(5000); // 5초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Shutdown wait interrupted: " + e.getMessage());
        }
        System.out.println("Shutdown complete.");
    }
}
