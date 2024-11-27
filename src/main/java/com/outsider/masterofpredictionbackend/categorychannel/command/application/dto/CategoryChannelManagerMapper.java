package com.outsider.masterofpredictionbackend.categorychannel.command.application.dto;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryChannelManagerMapper {


    @Mapping(target = "id", ignore = true) // ID는 자동 생성
    CategoryChannelManager toEntity(CategoryChannelManagerAssignRequestDTO requestDTO);
}
