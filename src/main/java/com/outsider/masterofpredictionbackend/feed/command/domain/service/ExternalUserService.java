package com.outsider.masterofpredictionbackend.feed.command.domain.service;


import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;

public interface ExternalUserService {
    User getUser(Long userId);
}