package com.outsider.masterofpredictionbackend.ranking.query;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScoreRankingMapper {

    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.displayName", target = "displayName")
    @Mapping(source = "user.userImg", target = "userImg")
    ScoreRankingDTO toScoreRankingDTO(ScoreRanking scoreRanking);
}
