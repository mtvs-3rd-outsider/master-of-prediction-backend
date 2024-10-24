package com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryChannelRepository extends JpaRepository<CategoryChannel, Long> {
    List<CategoryChannel> findByCategoryChannelStatus(CategoryChannelStatus status);
    Page<CategoryChannel> findByCategoryChannelStatus(CategoryChannelStatus status, Pageable pageable);
}
