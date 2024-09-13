package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChannelRequest {
    private String correlationId;
    private Long channelId;

    // 생성자, getter, setter
}
