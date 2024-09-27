package com.outsider.masterofpredictionbackend.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Optional;

@Slf4j
public abstract class GenericService<T, ID, S> {

    private static final Logger logger = LoggerFactory.getLogger(GenericService.class);

    protected final CrudRepository<T, ID> repository;  // CrudRepository로 변경
    private final Class<S> sourceClass;

    public GenericService(CrudRepository<T, ID> repository, Class<S> sourceClass) {
        this.repository = repository;
        this.sourceClass = sourceClass;
    }

    public void saveOrUpdate(JsonNode jsonNode, ID id, Class<T> targetClass, String... setIdMethodNames) throws IllegalAccessException {
        Optional<T> existingEntity = repository.findById(id);

        if (existingEntity.isPresent()) {
            // 기존 엔티티가 있는 경우 업데이트
            T updatedEntity = ReflectionUtil.mapJsonNodeToEntity(jsonNode, existingEntity.get(), sourceClass);
            repository.save(updatedEntity);
        } else {
            try {
                // 새로운 엔티티 생성 및 매핑
                T newEntity = targetClass.getDeclaredConstructor().newInstance();
                newEntity = ReflectionUtil.mapJsonNodeToEntity(jsonNode, newEntity, sourceClass);

                // 가변 인자가 없는 경우, ID 할당하지 않고 바로 저장
                if (setIdMethodNames == null || setIdMethodNames.length == 0) {
                    repository.save(newEntity); // ID를 설정하지 않고 그대로 저장
                } else {
                    // 가변 인자로 받은 메서드 이름 중 첫 번째 유효한 메서드를 사용하여 ID 설정
                    Method setIdMethod = null;
                    for (String methodName : setIdMethodNames) {
                        if (methodName != null && !methodName.isEmpty()) {
                            try {
                                setIdMethod = targetClass.getMethod(methodName, id.getClass());
                                setIdMethod.invoke(newEntity, id);  // ID 설정
                                break;  // 유효한 메서드 발견 시 루프 종료
                            } catch (NoSuchMethodException e) {
                                logger.warn("Method {} not found in class {}. Trying next method if available.", methodName, targetClass.getName());
                            }
                        }
                    }

                    // ID 설정 메서드를 찾았거나 찾지 못했을 경우에도 엔티티를 저장
                    repository.save(newEntity);
                }
            } catch (Exception e) {
                logger.error("Failed to create new entity instance or set ID", e);
            }
        }
    }


    public abstract void consume(ConsumerRecord<String, String> record, Acknowledgment ack);

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void retryProcessing(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            logger.info("Retrying message consumption for record: {}", record);
            ack.nack(Duration.ofSeconds(1));
        } catch (Exception e) {
            logger.error("Error during retry of record: {}", record, e);
            ack.nack(Duration.ofSeconds(1));
        }
    }
    public abstract void handleCreateOrUpdate(JsonNode jsonNode) throws IllegalAccessException;
    public abstract void handleDelete(JsonNode jsonNode);
}
