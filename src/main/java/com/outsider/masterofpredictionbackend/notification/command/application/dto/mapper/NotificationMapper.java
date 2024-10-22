package com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper;

import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.query.NotificationResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

   Notification ToEntity(NotificationDTO notificationDTO);
   NotificationResponseDTO ToDTO(Notification notification);
}
