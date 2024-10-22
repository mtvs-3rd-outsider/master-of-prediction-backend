package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final UserSearchRepository userSearchRepository;

    public UserQueryService(UserSearchRepository userSearchRepository) {
        this.userSearchRepository = userSearchRepository;
    }

    // 페이징을 위한 메서드: displayName으로 검색
    public Page<UserSearchModel> searchByDisplayName(String displayName, Pageable pageable) {
        return userSearchRepository.findByDisplayName(displayName, pageable);
    }

    // 페이징을 위한 메서드: userName으로 검색
    public Page<UserSearchModel> searchByUserName(String userName, Pageable pageable) {
        return userSearchRepository.findByUserNameContaining(userName, pageable);
    }


}
