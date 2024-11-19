package com.outsider.masterofpredictionbackend.betting.command.domain.repository;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingSearchModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BettingSearchRepository extends ElasticsearchRepository<BettingSearchModel, Long >, BettingSearchRepositoryCustom{
}
