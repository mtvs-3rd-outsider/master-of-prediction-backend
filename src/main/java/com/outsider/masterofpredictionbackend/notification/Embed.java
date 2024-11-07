package com.outsider.masterofpredictionbackend.notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Embed {
    private String title;
    private String description;
    private String url;
    private Integer color;
    private List<Field> fields;
    private Thumbnail thumbnail;
    private Footer footer;
    private String timestamp;
}
