package com.outsider.masterofpredictionbackend.common;

import lombok.Data;

@Data
public class ResponseMessage {
    private String message;
    private Object data; // 필요에 따라 추가 정보를 담을 수 있습니다.

    public ResponseMessage(String message) {
        this.message = message;
    }
}
