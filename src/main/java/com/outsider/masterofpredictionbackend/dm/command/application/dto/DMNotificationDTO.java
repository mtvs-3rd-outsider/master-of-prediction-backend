package com.outsider.masterofpredictionbackend.dm.command.application.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DMNotificationDTO {
    private String senderUserId;
    private String senderUserName;
    private String content;
    private String roomId;
    private String sent;
    private List<String> recipients;
}
