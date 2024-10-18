package com.outsider.masterofpredictionbackend.notification.command.application.service;


import com.outsider.masterofpredictionbackend.notification.command.application.exception.TokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FCMTokenService {


    private static final String TOKEN_PREFIX = "FCMTokens:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String userId, String token) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForSet().add(key, token);
    }

    public Set<String> getTokens(String userId) {
        String key = TOKEN_PREFIX + userId;
        // 키가 존재하는지 확인
        if (!redisTemplate.hasKey(key)) {
            return Collections.emptySet();
        }

        // 키의 타입이 Set인지 확인
        DataType type = redisTemplate.type(key);
        if (type != DataType.SET) {
            throw new IllegalArgumentException("The key " + key + " is not of type SET. Actual type: " + type);
        }

        Set<Object> members = redisTemplate.opsForSet().members(key);

        if (members == null) {
            return Collections.emptySet();
        }

        return members.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    public void deleteToken(String userId, String token) {
        String key = TOKEN_PREFIX + userId;
        Long removed = redisTemplate.opsForSet().remove(key, token);
        if (removed == null || removed == 0) {
            throw new TokenNotFoundException("Token not found for userId: " + userId);
        }
    }
}
