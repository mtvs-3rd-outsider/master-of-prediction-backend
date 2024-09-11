package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserQueryService {

    private final UserSearchRepository userSearchRepository;

    public UserQueryService(UserSearchRepository userSearchRepository) {
        this.userSearchRepository = userSearchRepository;
    }

    public List<UserSearchModel> searchByDisplayName(String displayName) {
        return userSearchRepository.findByDisplayNameContaining(displayName);
    }

    public List<UserSearchModel> searchByUserName(String userName) {
        return userSearchRepository.findByUserNameContaining(userName);
    }
}
