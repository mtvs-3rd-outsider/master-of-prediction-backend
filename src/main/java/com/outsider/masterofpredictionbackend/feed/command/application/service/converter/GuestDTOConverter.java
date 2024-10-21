package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.GuestDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestDTOConverter {

    public Guest toEntity(GuestDTO dto) {
        return new Guest(
                dto.getGuestId(),
                dto.getGuestPassword()
        );
    }


    public GuestDTO fromEntity(Guest entity) {
        return new GuestDTO(
                entity.getGuestId(),
                entity.getGuestPassword()
        );
    }
}
