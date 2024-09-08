package com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded;

public enum Location {
    KOREA,
    USA,
    JAPAN,
    CHINA,
    NONE;  // 선택 안 함을 의미하는 추가 값

    public static Location fromString(String location) {
        try {
            return Location.valueOf(location.toUpperCase()); // 대소문자 무시
        } catch (IllegalArgumentException e) {
            return Location.NONE; // 예외 발생 시 NONE 반환
        }
    }
}
