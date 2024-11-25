package com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper;

import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.query.NotificationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
   @Mapping(target = "id", ignore = true)
   Notification toEntity(NotificationDTO notificationDTO);
   NotificationResponseDTO ToDTO(Notification notification);

}
