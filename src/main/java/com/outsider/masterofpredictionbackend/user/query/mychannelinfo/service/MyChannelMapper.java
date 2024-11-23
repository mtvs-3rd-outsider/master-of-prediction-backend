package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MyChannelMapper {



    @Mapping(target = "userId", source = "id")
    MyChannelInfoQueryModel toDto(MyChannel entity);


    @Mapping(source = "userId", target = "id")
    MyChannel toEntity(MyChannelInfoQueryModel dto);
    // MyChannel -> MyChannelInfoQueryModel 매핑
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "website", target = "website")
    @Mapping(source = "bannerImg", target = "bannerImg")
    MyChannelInfoQueryModel toQueryModel(MyChannel myChannel);

    // 기존 MyChannelInfoQueryModel과 병합
    void updateFromQueryModel(MyChannelInfoQueryModel source, @MappingTarget MyChannelInfoQueryModel target);
}
