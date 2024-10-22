package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.QDMThread;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class DMThreadRepositoryCustomImpl implements DMThreadRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final DMThreadMapper dmThreadMapper;

    public DMThreadRepositoryCustomImpl(EntityManager entityManager, DMThreadMapper dmThreadMapper) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.dmThreadMapper = dmThreadMapper;
    }

    @Override
    public Page<DMThreadDTO> getThreadsBySenderId(Long senderId, Pageable pageable) {
        QDMThread dmThread = QDMThread.dMThread;
        QUser sender = QUser.user;
        QUser receiver = new QUser("receiver");
        // Fetch DMThread and related sender and receiver User entities
        List<Tuple> tuples = queryFactory
                .select(dmThread, sender, receiver)
                .from(dmThread)
                .leftJoin(sender).on(dmThread.id.senderId.eq(sender.id))
                .leftJoin(receiver).on(dmThread.id.receiverId.eq(receiver.id))
                .where(dmThread.id.senderId.eq(senderId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Map the Tuple result to DMThreadDTO using MapStruct
        List<DMThreadDTO> results = tuples.stream()
                .map(tuple -> dmThreadMapper.toDTO(
                        tuple.get(dmThread),
                        tuple.get(sender),
                        tuple.get(receiver)))
                .collect(Collectors.toList());

        // Fetch total count of DMThreads
        long total = queryFactory
                .select(dmThread.count())
                .from(dmThread)
                .where(dmThread.id.senderId.eq(senderId))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


}
