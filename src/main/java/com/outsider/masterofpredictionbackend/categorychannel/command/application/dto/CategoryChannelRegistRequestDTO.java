package com.outsider.masterofpredictionbackend.categorychannel.command.application.dto;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryChannelRegistRequestDTO {

    private String displayName;
    private String description;
    private String communityRule; // 추가 삭제가 자유롭도록 JSON 형식
    private CategoryChannelStatus categoryChannelStatus;
}
