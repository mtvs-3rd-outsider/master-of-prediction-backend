package com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded;

public enum Gender {
    MALE,
    FEMALE,
    UNSPECIFIED; // 유효하지 않은 값 처리

    public static Gender fromString(String gender) {
        try {
            return Gender.valueOf(gender.toUpperCase()); // 대소문자 무시
        } catch (IllegalArgumentException e) {
            return Gender.UNSPECIFIED; // 유효하지 않은 값일 경우
        }
    }
}

