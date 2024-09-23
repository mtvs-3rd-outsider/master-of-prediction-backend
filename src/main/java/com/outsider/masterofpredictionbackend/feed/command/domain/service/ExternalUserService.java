package com.outsider.masterofpredictionbackend.feed.command.domain.service;


import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;

public interface ExternalUserService {
    UserDTO getUser(String userId);
}