package com.outsider.masterofpredictionbackend.ranking.query;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.UserRanking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRankingMapper {

    UserRankingDTO toDTO(UserRanking userRanking);

    // UserRanking의 필드와 User의 필드를 명시적으로 매핑
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.displayName", target = "displayName")
    @Mapping(source = "user.userImg", target = "userImg")
    @Mapping(source = "userRanking.userId", target = "userId")
    @Mapping(source = "userRanking.points", target = "points")
    @Mapping(source = "userRanking.rank", target = "rank")
    @Mapping(source = "userRanking.lastUpdated", target = "lastUpdated")
    UserRankingDTO toDTO(UserRanking userRanking, User user);
    UserRanking toEntity(UserRankingDTO userRankingDTO);
}
