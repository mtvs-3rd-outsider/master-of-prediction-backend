package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import java.lang.reflect.Field;

public class GenericEvent<T> {
    private final T eventData; // 제네릭으로 받은 데이터

    public GenericEvent(T eventData) {
        this.eventData = eventData;
    }

    public T getEventData() {
        return eventData;
    }

    // 리플렉션을 사용해 필드 값을 동적으로 출력
    public void printFields() {
        Field[] fields = eventData.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // private 필드도 접근 가능하게 설정
            try {
                System.out.println(field.getName() + ": " + field.get(eventData));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
