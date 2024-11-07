package com.outsider.masterofpredictionbackend.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {
    private String name;
    private String value;
    private Boolean inline;
}
