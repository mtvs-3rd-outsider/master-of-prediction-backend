package com.outsider.masterofpredictionbackend.dm.command.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;

public interface NotificationClient {
    void sendNotification(UpdateMessageDTO updateMessageDTO) throws JsonProcessingException;
}
