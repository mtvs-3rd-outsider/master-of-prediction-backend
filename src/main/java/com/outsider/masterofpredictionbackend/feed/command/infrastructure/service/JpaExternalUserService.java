package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import org.springframework.stereotype.Service;

@Service
public class JpaExternalUserService implements ExternalUserService {

    @Override
    public User getUser(Long userId) {
        return null;
    }
}
