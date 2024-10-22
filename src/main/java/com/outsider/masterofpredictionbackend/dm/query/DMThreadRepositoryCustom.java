package com.outsider.masterofpredictionbackend.dm.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMThreadRepositoryCustom {
    Page<DMThreadDTO> getThreadsBySenderId(Long senderId, Pageable pageable);
}
