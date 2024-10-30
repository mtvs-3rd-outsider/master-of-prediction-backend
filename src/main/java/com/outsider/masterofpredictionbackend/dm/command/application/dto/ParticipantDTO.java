package com.outsider.masterofpredictionbackend.dm.command.application.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantDTO {
    private Long userId;
    private String userName;
    private String avatarUrl; // 아바타 이미지 URL
    private String displayName;
}
