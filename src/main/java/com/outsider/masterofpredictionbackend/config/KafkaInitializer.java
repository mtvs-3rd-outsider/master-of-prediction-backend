package com.outsider.masterofpredictionbackend.config;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class KafkaInitializer implements SmartLifecycle {

    private boolean isRunning = false;

    @Override
    public void start() {
        // Kafka 관련 초기화 로직
        System.out.println("Kafka initialization logic executed.");
        isRunning = true;
    }

    @Override
    public void stop() {
        // 리소스 종료 로직
        System.out.println("Kafka stopped.");
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        // 음수로 설정하면 다른 초기화 로직보다 나중에 실행됨
        return Integer.MAX_VALUE;
    }
}
