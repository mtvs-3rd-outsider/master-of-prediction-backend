package com.outsider.masterofpredictionbackend.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static AtomicLong sequence = new AtomicLong(0);
    private static long lastTimestamp = -1L;

    public synchronized static long generateId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp == lastTimestamp) {
            sequence.incrementAndGet();  // 같은 타임스탬프일 경우 시퀀스 증가
        } else {
            sequence.set(0);  // 새로운 타임스탬프일 경우 시퀀스 초기화
            lastTimestamp = timestamp;
        }

        // 타임스탬프와 시퀀스를 결합하여 Long ID 생성
        return (timestamp << 20) | sequence.get();
    }
}

