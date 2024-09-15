package com.outsider.masterofpredictionbackend.channelsubscribe.query.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.request.UserRequest;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.UserResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.USER_REQUEST_TOPIC;
import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.USER_RESPONSE_TOPIC;

@Service
public class UserServiceClient {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<UserResponse>> pendingRequests = new ConcurrentHashMap<>();

    public UserServiceClient(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<UserResponse> getUserById(Long userId) {
        String correlationId = UUID.randomUUID().toString();
        UserRequest request = new UserRequest(correlationId, userId);

        CompletableFuture<UserResponse> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        try {
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(USER_REQUEST_TOPIC, message);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    @KafkaListener(topics = USER_RESPONSE_TOPIC, groupId = "subscription-service")
    public void consume(String message) {
        try {
            UserResponse response = objectMapper.readValue(message, UserResponse.class);
            CompletableFuture<UserResponse> future = pendingRequests.remove(response.getCorrelationId());
            if (future != null) {
                future.complete(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
