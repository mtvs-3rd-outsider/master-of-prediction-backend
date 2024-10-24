package com.outsider.masterofpredictionbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
//@EnableKafkaStreams
@EnableKafka
@EnableScheduling
public class MasterOfPredictionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasterOfPredictionBackendApplication.class, args);
	}

}
