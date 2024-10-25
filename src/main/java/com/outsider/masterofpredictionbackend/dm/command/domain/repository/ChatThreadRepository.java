package com.outsider.masterofpredictionbackend.dm.command.domain.repository;


import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> , DMThreadRepositoryCustom {
    @Query("SELECT t FROM ChatThread t JOIN Participant p ON t = p.thread WHERE p.userId = :userId")
    Page<ChatThread> getThreadsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM ChatThread t JOIN t.participants p " +
            "WHERE p.userId IN (:currentUserId, :otherUserId) " +
            "GROUP BY t " +
            "HAVING COUNT(p.userId) = 2 AND t.isGroupThread = false")
    Optional<ChatThread> findOneToOneThread(@Param("currentUserId") Long currentUserId, @Param("otherUserId") Long otherUserId);
}
