package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.event;


import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelSubscriptionEvent {
    private ChannelSubscribeRequestDTO dto;
    private boolean validationSuccess;
}
