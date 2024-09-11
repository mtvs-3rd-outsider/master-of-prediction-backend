package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserSearchModel, Long> {

    List<UserSearchModel> findByDisplayNameContaining(String displayName);
    List<UserSearchModel> findByUserNameContaining(String userName);
}
