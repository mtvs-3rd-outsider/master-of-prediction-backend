package com.outsider.masterofpredictionbackend.feed.command.domain.service;


import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
public interface ExternalUserService {
    UserDTO getUser(Long userId);
}