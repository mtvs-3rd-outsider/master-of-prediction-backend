package com.outsider.masterofpredictionbackend.notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DiscordMessage {
    private String content;
    private List<Embed> embeds;
}
