package com.outsider.masterofpredictionbackend.util;

public interface DTOConverter<D,E> {
    E toEntity(D dto);
    D fromEntity(E entity);
    Class<D> getDtoClass();
}
