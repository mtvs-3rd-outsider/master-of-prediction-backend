package com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryChannelRepository extends JpaRepository<CategoryChannel, Long> {
}
