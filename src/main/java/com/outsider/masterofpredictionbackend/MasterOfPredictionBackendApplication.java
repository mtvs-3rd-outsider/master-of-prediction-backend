package com.outsider.masterofpredictionbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableJpaAuditing
//@EnableKafkaStreams
@EnableKafka
public class MasterOfPredictionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasterOfPredictionBackendApplication.class, args);
	}

}
