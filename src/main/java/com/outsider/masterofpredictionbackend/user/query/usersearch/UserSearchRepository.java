package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserSearchModel, Long > {

    Page<UserSearchModel> findByDisplayNameContaining(String displayName, Pageable pageable);
    Page<UserSearchModel> findByUserNameContaining(String userName, Pageable pageable);
}
