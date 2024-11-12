package com.outsider.masterofpredictionbackend.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Footer {
    private String text;
    private String icon_url;
}
