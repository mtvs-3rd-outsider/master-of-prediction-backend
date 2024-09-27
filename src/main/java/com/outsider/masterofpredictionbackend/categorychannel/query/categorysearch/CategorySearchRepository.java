package com.outsider.masterofpredictionbackend.categorychannel.query.categorysearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategorySearchRepository extends ElasticsearchRepository<CategorySearchModel, Long > {


    Page<CategorySearchModel> findByDisplayName(String displayName, Pageable pageable);
//    Page<CategorySearchModel> findByUserNameContaining(String userName, Pageable pageable);
}
