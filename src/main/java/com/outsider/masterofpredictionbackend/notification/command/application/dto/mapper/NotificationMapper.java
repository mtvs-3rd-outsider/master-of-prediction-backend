package com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper;

import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.query.NotificationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
   @Mapping(target = "id", ignore = true) // ID는 자동 생성되므로 무시
   @Mapping(target = "additionalData", source = "additionalData") // 추가 데이터 매핑
   Notification toEntity(NotificationDTO notificationDTO);

   @Mapping(target = "additionalData", source = "additionalData") // 추가 데이터 매핑
   NotificationResponseDTO toDTO(Notification notification);
}
