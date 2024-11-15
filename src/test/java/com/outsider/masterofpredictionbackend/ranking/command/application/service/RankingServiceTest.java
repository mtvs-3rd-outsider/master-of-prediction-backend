package com.outsider.masterofpredictionbackend.ranking.command.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.outsider.masterofpredictionbackend.bettingorder.query.dto.UserPredictionResultDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "predictionResult" })
class UserPointsChangeConsumerTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RankingService rankingService;

    private CountDownLatch latch;

    @BeforeEach
    void setup() {
        kafkaTemplate.setDefaultTopic("predictionResult");
        latch = new CountDownLatch(1);
    }

    @Test
    void testConsumePredictionResult() throws JsonProcessingException, InterruptedException {
        // Mock JSON 이벤트 생성
        UserPredictionResultDTO fakeResult = new UserPredictionResultDTO(1L, "win");
        String fakeMessage = objectMapper.writeValueAsString(List.of(fakeResult));

        // Kafka 이벤트 전송
        kafkaTemplate.send("predictionResult", fakeMessage);

        // 랭킹 업데이트 로직이 호출되었는지 확인
        latch.await(5, TimeUnit.SECONDS); // 메시지 처리 대기

        // 추가로 랭킹이 올바르게 업데이트되었는지 검증하는 로직을 작성
        // 예를 들어, rankingService에서 호출된 메서드 또는 데이터 검증
    }
}

