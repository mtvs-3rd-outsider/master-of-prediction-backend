package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import com.outsider.masterofpredictionbackend.util.GenericService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyChannelInfoChannelPartService extends GenericService<MyChannelInfoQueryModel, Long, MyChannel> {

    private static final Logger logger = LoggerFactory.getLogger(MyChannelInfoChannelPartService.class);
    private final ObjectMapper mapper;

    public MyChannelInfoChannelPartService(MyChannelInfoRepository repository, ObjectMapper mapper) {
        super(repository, MyChannel.class);
        this.mapper = mapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.my_channel")
    @Transactional
    public void consumeMyChannel(ConsumerRecord<String, String> record) {
        String consumedValue = record.value();

        if (consumedValue == null) {
            logger.error("Consumed record value is null for record: {}", record);
            return;
        }

        try {
            JsonNode jsonNode = mapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            String operation = payload.get("op").asText().substring(0, 1);
            JsonNode after = payload.path("after");
            JsonNode before = payload.path("before");

            switch (operation) {
                case "c":
                case "u":
                    handleCreateOrUpdate(after);
                    break;
                case "d":
                    handleDelete(before);
                    break;
                default:
                    logger.warn("Unknown operation type: {}", operation);
            }
        } catch (Exception e) {
            logger.error("Unexpected error occurred while consuming record: {}", record, e);
        }
    }

    public void handleCreateOrUpdate(JsonNode jsonNode) {
        Long channelId = jsonNode.get("channel_id").asLong();
        saveOrUpdate(jsonNode, channelId, MyChannelInfoQueryModel.class);
    }

    public void handleDelete(JsonNode jsonNode) {
        Long channelId = jsonNode.get("channel_id").asLong();
        deleteById(channelId);
    }
}
