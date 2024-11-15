package com.outsider.masterofpredictionbackend.user.query.usersearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSearchRepositoryCustom {
    Page<UserSearchModel> findByDisplayNameOrContaining(String displayName, Pageable pageable);
}
