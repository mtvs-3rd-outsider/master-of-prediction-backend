package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import com.outsider.masterofpredictionbackend.util.GenericService;
import com.outsider.masterofpredictionbackend.util.IDs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//TODO: 카프카 listener 변경하고, 상속 받고 생성자 만들고 아래 TODO쪽도 변경하면 끝
@Service
public class MyChannelInfoCDCUserPartConsumer extends GenericService<MyChannelInfoQueryModel, Long, User> {

    private static final Logger logger = LoggerFactory.getLogger(MyChannelInfoCDCUserPartConsumer.class);
    private final ObjectMapper mapper;
    public MyChannelInfoCDCUserPartConsumer(MyChannelInfoRepository repository, ObjectMapper mapper) {
        super(repository, User.class);
        this.mapper = mapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.user",groupId = "my-channel-info-user-group")
    @Transactional
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        System.out.printf("Received message: %s, From partition: %d, With offset: %d, From topic: %s%n",
                record.value(), record.partition(), record.offset(), record.topic());
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

            // 수동으로 오프셋 커밋
            ack.acknowledge();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while consuming record: {}", record, e);

            // 재처리 로직 - 메시지 처리 실패 시 재시도하거나 별도의 큐에 추가하는 방식으로 처리
            retryProcessing(record,ack);
        }
    }

    public void handleCreateOrUpdate(JsonNode jsonNode) throws IllegalAccessException {
        //TODO: 여기에 ID 바꾸면됨
        Long userId = jsonNode.get(IDs.USER_ID).asLong();
        saveOrUpdate(jsonNode, userId, MyChannelInfoQueryModel.class);
    }

    public void handleDelete(JsonNode jsonNode) {
        //TODO: 여기에 ID 바꾸면됨
        Long userId = jsonNode.get(IDs.USER_ID).asLong();
        deleteById(userId);
    }
}
