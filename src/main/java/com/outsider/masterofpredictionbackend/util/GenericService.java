package com.outsider.masterofpredictionbackend.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class GenericService<T, ID, S> {

    private static final Logger logger = LoggerFactory.getLogger(GenericService.class);

    protected final CrudRepository<T, ID> repository;  // CrudRepository로 변경
    private final Class<S> sourceClass;

    public GenericService(CrudRepository<T, ID> repository, Class<S> sourceClass) {
        this.repository = repository;
        this.sourceClass = sourceClass;
    }

    public void saveOrUpdate(JsonNode jsonNode, ID id, Class<T> targetClass) {
        Optional<T> existingEntity = repository.findById(id);

        if (existingEntity.isPresent()) {
            T updatedEntity = ReflectionUtil.mapJsonNodeToEntity(jsonNode, existingEntity.get(), sourceClass);
            repository.save(updatedEntity);
        } else {
            try {
                T newEntity = targetClass.getDeclaredConstructor().newInstance();
                newEntity = ReflectionUtil.mapJsonNodeToEntity(jsonNode, newEntity, sourceClass);
                repository.save(newEntity);
            } catch (Exception e) {
                logger.error("Failed to create new entity instance", e);
            }
        }
    }

    public abstract void consume(ConsumerRecord<String, String> record, Consumer<String, String> consumer);

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void retryProcessing(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        try {
            logger.info("Retrying message consumption for record: {}", record);
            consume(record, consumer);
        } catch (Exception e) {
            logger.error("Error during retry of record: {}", record, e);
        }
    }
    public abstract void handleCreateOrUpdate(JsonNode jsonNode);
    public abstract void handleDelete(JsonNode jsonNode);
}
