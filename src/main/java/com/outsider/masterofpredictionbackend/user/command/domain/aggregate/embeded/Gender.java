package com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded;

public enum Gender {
    MALE,
    FEMALE;
    public static Gender fromString(String gender) {
        try {
            return Gender.valueOf(gender.toUpperCase()); // 대소문자 무시
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }
}
