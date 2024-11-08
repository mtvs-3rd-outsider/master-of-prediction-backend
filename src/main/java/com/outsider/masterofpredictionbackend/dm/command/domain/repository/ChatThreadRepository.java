package com.outsider.masterofpredictionbackend.dm.command.domain.repository;


import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {
    @Query("SELECT t FROM ChatThread t JOIN Participant p ON t = p.thread WHERE p.user.id = :userId AND p.isActive = true")
    Page<ChatThread> getThreadsByUserId(@Param("userId") Long userId, Pageable pageable);
    @Query("SELECT t FROM ChatThread t JOIN t.participants p " +
            "WHERE p.user.id IN (:currentUserId, :otherUserId) " +
            "GROUP BY t " +
            "HAVING COUNT(p.user.id) = 2 AND t.isGroupThread = false")
    Optional<ChatThread> findOneToOneThread(@Param("currentUserId") Long currentUserId, @Param("otherUserId") Long otherUserId);


}
