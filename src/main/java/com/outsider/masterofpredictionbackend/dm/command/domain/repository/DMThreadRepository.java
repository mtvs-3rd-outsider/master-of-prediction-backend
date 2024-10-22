package com.outsider.masterofpredictionbackend.dm.command.domain.repository;


import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DMThreadRepository extends JpaRepository<DMThread, DMThreadKey> , DMThreadRepositoryCustom {
    Optional<DMThread> findById(DMThreadKey id);


    Page<DMThread> findByIdSenderId(Long senderId, Pageable pageable);
}
