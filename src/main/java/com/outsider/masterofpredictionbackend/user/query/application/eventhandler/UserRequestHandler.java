package com.outsider.masterofpredictionbackend.user.query.application.eventhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.request.UserRequest;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.UserResponse;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.USER_REQUEST_TOPIC;
import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.USER_RESPONSE_TOPIC;

@Service
public class UserRequestHandler {

    private final UserCommandRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UserRequestHandler(UserCommandRepository userRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = USER_REQUEST_TOPIC, groupId = "user-service")
    public void consume(String message) {
        try {
            UserRequest request = objectMapper.readValue(message, UserRequest.class);
            Long userId = request.getUserId();

            // 사용자 정보 조회
            User user = userRepository.findById(userId).orElse(null);

            UserResponse response = new UserResponse(
                    request.getCorrelationId(),
                    userId,
                    user != null ? user.getUserName() : null,
                    user != null ? user.getDisplayName() : null,
                    user != null ? user.getUserImg() : null
            );

            String responseMessage = objectMapper.writeValueAsString(response);
            kafkaTemplate.send(USER_RESPONSE_TOPIC, responseMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
