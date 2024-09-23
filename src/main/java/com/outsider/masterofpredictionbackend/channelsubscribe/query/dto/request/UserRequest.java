package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRequest {
    private String correlationId;
    private Long userId;

    // 생성자, getter, setter
}
