package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository;



import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.QMyChannel;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.QUser;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.QMyChannelInfoQueryModel;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserChannelInfoRepositoryCustomImpl implements UserChannelInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public UserChannelInfoRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);

    }


    @Override
    public Optional<MyChannelInfoQueryModel> findMyChannelInfoByUserId(Long userId) {
        QUser user = QUser.user;
        QMyChannel channel = QMyChannel.myChannel;

        return Optional.ofNullable(
                queryFactory.select(new QMyChannelInfoQueryModel(
                                user.id.as("userId"),
                                user.userName,
                                user.email.as("userEmail"),
                                user.displayName,
                                channel.bio,
                                user.location.stringValue(),
                                channel.website,
                                user.birthday.stringValue(),
                                channel.bannerImg,
                                user.userImg,
                                user.tier.name,
                                user.tier.level
//                                channel.transactions,
//                                channel.profitRate,
//                                channel.positionValue,
//                                channel.tradeCount,
//                                channel.followingCount,
//                                channel.followersCount
                        ))
                        .from(user)
                        .leftJoin(channel).on(user.id.eq(channel.id))
                        .where(user.id.eq(userId))
                        .fetchOne()
        );
    }
}
