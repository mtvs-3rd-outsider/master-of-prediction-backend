package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.GuestDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.springframework.stereotype.Component;

@Component
public class GuestDTOConverter implements DTOConverter<GuestDTO, Guest> {
    @Override
    public Guest toEntity(GuestDTO dto) {
        return new Guest(
                dto.getGuestId(),
                dto.getGuestPassword()
        );
    }

    @Override
    public GuestDTO fromEntity(Guest entity) {
        return new GuestDTO(
                entity.getGuestId(),
                entity.getGuestPassword()
        );
    }

    @Override
    public Class<GuestDTO> getDtoClass() {
        return GuestDTO.class;
    }
}
