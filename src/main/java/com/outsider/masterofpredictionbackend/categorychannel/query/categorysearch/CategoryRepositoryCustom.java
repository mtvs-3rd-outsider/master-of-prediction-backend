package com.outsider.masterofpredictionbackend.categorychannel.query.categorysearch;

import com.outsider.masterofpredictionbackend.user.query.usersearch.UserSearchModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {
    Page<CategorySearchModel> findByDisplayNameOrContaining(String displayName, Pageable pageable);
}
