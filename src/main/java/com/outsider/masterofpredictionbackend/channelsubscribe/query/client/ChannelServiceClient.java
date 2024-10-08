package com.outsider.masterofpredictionbackend.channelsubscribe.query.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.request.ChannelRequest;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.ChannelResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.CHANNEL_REQUEST_TOPIC;
import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.CHANNEL_RESPONSE_TOPIC;

@Service
public class ChannelServiceClient {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<ChannelResponse>> pendingRequests = new ConcurrentHashMap<>();

    public ChannelServiceClient(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<ChannelResponse> getChannelById(Long channelId) {
        String correlationId = UUID.randomUUID().toString();
        ChannelRequest request = new ChannelRequest(correlationId, channelId);

        CompletableFuture<ChannelResponse> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        try {
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(CHANNEL_REQUEST_TOPIC, message);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    @KafkaListener(topics = CHANNEL_RESPONSE_TOPIC, groupId = "subscription-service")
    public void consume(String message, Acknowledgment ack) {
        try {
            ChannelResponse response = objectMapper.readValue(message, ChannelResponse.class);
            CompletableFuture<ChannelResponse> future = pendingRequests.remove(response.getCorrelationId());
            if (future != null) {
                future.complete(response);
            }
            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
            ack.nack(Duration.ofSeconds(1));
        }
    }
}
