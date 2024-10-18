package com.outsider.masterofpredictionbackend.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    // 초 단위 타임스탬프를 사용할 것이므로 시퀀스의 범위를 안전한 범위로 제한
    private static AtomicLong sequence = new AtomicLong(0);
    private static long lastTimestamp = -1L;
    private static final long MAX_SEQUENCE = 1023L; // 시퀀스의 최대값 (10비트)

    public synchronized static long generateId() {
        long timestamp = System.currentTimeMillis() / 1000L; // 초 단위로 타임스탬프 사용

        if (timestamp == lastTimestamp) {
            long seq = sequence.incrementAndGet();
            if (seq > MAX_SEQUENCE) {
                // 시퀀스가 한도를 넘으면 새 타임스탬프가 나올 때까지 대기
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis() / 1000L;
                }
                sequence.set(0);
            }
        } else {
            sequence.set(0);  // 새로운 타임스탬프일 경우 시퀀스 초기화
            lastTimestamp = timestamp;
        }

        // 타임스탬프와 시퀀스를 결합하여 Long ID 생성
        // 타임스탬프(42비트) + 시퀀스(10비트) = 52비트 내로 JavaScript에서 안전하게 사용할 수 있음
        return (timestamp << 10) | sequence.get();
    }
}