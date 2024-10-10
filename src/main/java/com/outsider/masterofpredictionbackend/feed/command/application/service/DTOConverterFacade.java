package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DTOConverterFacade {
    private final Map<Class<?>, DTOConverter<?, ?>> converters;

    public DTOConverterFacade(List<DTOConverter<?, ?>> converterList) {
        this.converters = converterList.stream()
                .collect(Collectors.toMap(DTOConverter::getDtoClass, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <D, E> DTOConverter<D, E> getConverter(Class<D> dtoClass) {
        DTOConverter<?, ?> converter = converters.get(dtoClass);
        if (converter == null) {
            throw new IllegalArgumentException("Unsupported DTO class: " + dtoClass.getName());
        }
        return (DTOConverter<D, E>) converter;
    }

    public <D, E> E toEntity(D dto) {
        DTOConverter<D, E> converter = getConverter((Class<D>) dto.getClass());
        return converter.toEntity(dto);
    }

    public <D, E> D fromEntity(E entity, Class<D> dtoClass) {
        DTOConverter<D, E> converter = getConverter(dtoClass);
        return converter.fromEntity(entity);
    }
}
