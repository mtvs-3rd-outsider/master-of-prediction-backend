package com.outsider.masterofpredictionbackend.categorychannel.command.application.dto;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.ManagerRole;
import lombok.Data;

@Data
public class CategoryChannelManagerAssignRequestDTO {

    private Long categoryChannelId;
    private Long userId;
    private ManagerRole role;


}
