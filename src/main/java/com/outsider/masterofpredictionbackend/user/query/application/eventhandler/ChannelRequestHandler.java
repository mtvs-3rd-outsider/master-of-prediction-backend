package com.outsider.masterofpredictionbackend.user.query.application.eventhandler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.request.ChannelRequest;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.ChannelResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.CHANNEL_REQUEST_TOPIC;
import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.CHANNEL_RESPONSE_TOPIC;

@Service
public class ChannelRequestHandler {

    private final CategoryChannelRepository categoryChannelRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ChannelRequestHandler(CategoryChannelRepository categoryChannelRepository,
                                 KafkaTemplate<String, String> kafkaTemplate,
                                 ObjectMapper objectMapper) {
        this.categoryChannelRepository = categoryChannelRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = CHANNEL_REQUEST_TOPIC, groupId = "category-channel-service")
    public void consume(String message) {
        try {
            // Kafka로부터 받은 메시지를 ChannelRequest로 변환
            ChannelRequest request = objectMapper.readValue(message, ChannelRequest.class);
            Long channelId = request.getChannelId();

            // 카테고리 채널 정보 조회
            CategoryChannel categoryChannel = categoryChannelRepository.findById(channelId).orElse(null);

            // 응답 객체 생성
            ChannelResponse response = new ChannelResponse(
                    categoryChannel != null ? categoryChannel.getDisplayName() : null,
                    request.getCorrelationId(),
                    channelId,
                    null,
                    categoryChannel != null ? categoryChannel.getImageUrl() : null
            );

            // 응답 메시지를 JSON으로 변환
            String responseMessage = objectMapper.writeValueAsString(response);

            // Kafka로 응답 메시지 전송
            kafkaTemplate.send(CHANNEL_RESPONSE_TOPIC, responseMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
