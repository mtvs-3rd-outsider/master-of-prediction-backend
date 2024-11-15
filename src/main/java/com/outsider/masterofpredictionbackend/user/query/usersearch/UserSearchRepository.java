package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserSearchRepository extends ElasticsearchRepository<UserSearchModel, Long > ,UserSearchRepositoryCustom {

    Page<UserSearchModel> findByUserNameContaining(String userName, Pageable pageable);
}
