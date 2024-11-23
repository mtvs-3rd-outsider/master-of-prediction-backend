package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserForMyChannelInfoMapper {


    // User 데이터를 MyChannelInfoQueryModel로 매핑
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "email", target = "userEmail")
    @Mapping(source = "displayName", target = "displayName")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "points", target = "points")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "joinDate", target = "joinedDate")
    @Mapping(source = "userImg", target = "userImg")
    @Mapping(source = "tier.name", target = "tierName")
    @Mapping(source = "tier.level", target = "tierLevel")
    MyChannelInfoQueryModel toQueryModel(User user);
    // 기존 MyChannelInfoQueryModel과 병합
    void updateFromQueryModel(MyChannelInfoQueryModel source, @MappingTarget MyChannelInfoQueryModel target);
    // 기존 데이터와 병합
    void updateFromUser(User source, @MappingTarget MyChannelInfoQueryModel target);
    // 기존 데이터와 병합
    void updateFromDto(User source, @MappingTarget User target);
}
