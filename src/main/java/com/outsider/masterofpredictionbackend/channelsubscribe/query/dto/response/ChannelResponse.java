package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChannelResponse {
    private String displayName;
    private String correlationId;
    private Long channelId;
    private String channelName;
    private String channelImageUrl;

    // 생성자, getter, setter
}
