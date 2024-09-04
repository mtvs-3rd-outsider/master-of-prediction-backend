package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoViewModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MyChannelInfoChannelPartService {
    private final MyChannelInfoRepository repository;
    private final ObjectMapper mapper;

    public MyChannelInfoChannelPartService(MyChannelInfoRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.my_channel")
    @Transactional
    public void consumeMyChannel(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String consumedValue = record.value();
        var jsonNode = mapper.readTree(consumedValue);
        JsonNode payload = jsonNode.path("payload");
        String op = payload.get("op").toString().substring(1, 2);
        JsonNode after = payload.path("after");
        JsonNode before = payload.path("before");

        switch (op) {
            case "c":
                handleCreate(after);
                break;
            case "u":
                handleUpdate(after);
                break;
            case "d":
                handleDelete(before);
                break;
            default:
                break;
        }
    }

    private void handleCreate(JsonNode after) {
        MyChannelInfoViewModel mongoChannel = MyChannelInfoViewModel.builder()
                .id(after.get("id").asLong())
                .bio(after.has("bio") ? after.get("bio").asText() : null)
                .displayName(after.get("display_name").asText())
                .website(after.has("website") ? after.get("website").asText() : null)
                .followersCount(after.get("followers_count").asInt())
                .followingCount(after.get("following_count").asInt())
                .build();

        repository.save(mongoChannel);
    }

    private void handleUpdate(JsonNode after) {
        Long id = after.get("id").asLong();
        Optional<MyChannelInfoViewModel> optionalChannel = repository.findById(id);

        if (optionalChannel.isPresent()) {
            MyChannelInfoViewModel existingChannel = optionalChannel.get();

            MyChannelInfoViewModel updatedChannel = MyChannelInfoViewModel.builder()
                    .id(existingChannel.getId()) // 기존 ID 유지
                    .bio(after.has("bio") ? after.get("bio").asText() : existingChannel.getBio())
                    .displayName(after.get("display_name").asText())
                    .website(after.has("website") ? after.get("website").asText() : existingChannel.getWebsite())
                    .followersCount(after.get("followers_count").asInt())
                    .followingCount(after.get("following_count").asInt())
                    .build();

            repository.save(updatedChannel);
        }
    }

    private void handleDelete(JsonNode before) {
        Long id = before.get("id").asLong();
        repository.deleteById(id);
    }
}
