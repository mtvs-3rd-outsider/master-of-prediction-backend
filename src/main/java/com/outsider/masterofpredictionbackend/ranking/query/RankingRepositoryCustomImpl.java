package com.outsider.masterofpredictionbackend.ranking.query;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.QUserRanking.userRanking;
import static com.outsider.masterofpredictionbackend.user.command.domain.aggregate.QUser.user;

@Repository
@Transactional
public class RankingRepositoryCustomImpl implements RankingRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final UserRankingMapper userRankingMapper; // MapStruct Mapper

    public RankingRepositoryCustomImpl(EntityManager entityManager, UserRankingMapper userRankingMapper) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.userRankingMapper = userRankingMapper;
    }
    @Override
    public Page<UserRankingDTO> findUserRankingsWithUserName(Pageable pageable) {
        // Fetch UserRanking and User entities using Tuple
        List<Tuple> tuples = queryFactory
                .select(userRanking, user)
                .from(userRanking)
                .leftJoin(user).on(userRanking.userId.eq(user.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Map the Tuple result to UserRankingDTO using MapStruct
        List<UserRankingDTO> results = tuples.stream()
                .map(tuple -> userRankingMapper.toDTO(tuple.get(userRanking), tuple.get(user)))
                .collect(Collectors.toList());

        // Fetch total count of UserRanking
        long total = queryFactory
                .select(userRanking.count())
                .from(userRanking)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Optional<UserRankingDTO> findUserRankingByUserId(Long userId) {
        // Fetch the UserRanking and User entities using a join
        Tuple tuple = queryFactory
                .select(userRanking, user)
                .from(userRanking)
                .leftJoin(user).on(userRanking.userId.eq(user.id))
                .where(userRanking.userId.eq(userId))
                .fetchOne();

        if (tuple == null) {
            return Optional.empty();
        }

        // Map the Tuple result to UserRankingDTO using MapStruct
        UserRankingDTO result = userRankingMapper.toDTO(tuple.get(userRanking), tuple.get(user));
        return Optional.of(result);
    }
}

