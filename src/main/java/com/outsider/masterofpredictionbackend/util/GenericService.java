package com.outsider.masterofpredictionbackend.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public abstract class GenericService<T, ID, S> {
    private static final Logger logger = LoggerFactory.getLogger(GenericService.class);

    protected final MongoRepository<T, ID> repository;
    private final Class<S> sourceClass;

    public GenericService(MongoRepository<T, ID> repository, Class<S> sourceClass) {
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

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
