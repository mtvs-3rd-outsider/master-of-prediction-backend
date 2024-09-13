package com.outsider.masterofpredictionbackend.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);
    private static final LocalDate EPOCH_DATE = LocalDate.of(1970, 1, 1); // 기준 날짜
    private static final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>(); // 필드 캐싱을 위한 맵

    public static <S, T> T mapJsonNodeToEntity(JsonNode node, T targetEntity, Class<S> sourceClass) {
        // 타겟 및 소스 필드 캐싱
        Map<String, Field> targetFieldMap = getFieldMapJsonOrField(targetEntity.getClass());
        Map<String, Field> sourceFieldMap = getFieldMapColumnOrField(sourceClass);

        for (Map.Entry<String, Field> targetEntry : targetFieldMap.entrySet()) {
            Field targetField = targetEntry.getValue();
            String jsonFieldName = getJsonFieldName(targetField); // 타겟 JSON 필드 이름 가져오기
            logger.info("Mapping field: {} to JSON field: {}", targetField.getName(), jsonFieldName);

            Field sourceField = sourceFieldMap.get(jsonFieldName); // 해시맵에서 필드 가져오기
            if (sourceField != null) {
                try {
                    Object sourceValue = getValueForField(node, sourceField, jsonFieldName);
                    logger.info("Source value for field {}: {}", jsonFieldName, sourceValue);

                    Object convertedValue = convertValue(sourceValue, targetField.getType());
                    logger.info("Converted value for field {}: {}", targetField.getName(), convertedValue);

                    if (convertedValue != null) {
                        targetField.set(targetEntity, convertedValue);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("Failed to set field value for field: {}", targetField.getName(), e);
                }
            } else {
                logger.warn("JSON does not contain field: {}", jsonFieldName); // 필드가 없는 경우 경고 로그
            }
        }

        return targetEntity;
    }

    // 클래스의 필드를 해시맵으로 변환하여 캐싱
    private static Map<String, Field> getFieldMapJsonOrField(Class<?> clazz) {
        if (!fieldCache.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                String jsonFieldName = getJsonFieldName(field);
                fieldMap.put(jsonFieldName, field); // 필드 이름과 Field 객체를 매핑
            }
            fieldCache.put(clazz, fieldMap); // 캐싱
        }
        return fieldCache.get(clazz);
    }
    // 클래스의 필드를 @Column 이름을 기준으로 해시맵에 변환하여 캐싱
    private static Map<String, Field> getFieldMapColumnOrField(Class<?> clazz) {
        if (!fieldCache.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = getColumnName(field); // @Column 어노테이션에서 이름을 가져오기
                fieldMap.put(columnName, field); // @Column 이름과 Field 객체를 매핑
            }
            fieldCache.put(clazz, fieldMap); // 캐싱
        }
        return fieldCache.get(clazz);
    }

    // @Column 어노테이션의 이름을 가져오기
    private static String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            return column.name(); // @Column 어노테이션이 있으면 그 값을 사용
        }
        return field.getName(); // 없으면 필드 이름을 그대로 사용
    }
    // 필드에서 JSON 필드 이름 가져오기
    private static String getJsonFieldName(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        return field.getName();
    }

    // JSON 노드에서 값 추출 (Source 필드의 타입에 맞게 변환)
    private static Object getValueForField(JsonNode node, Field field, String jsonFieldName) {
        Class<?> fieldType = field.getType();
        JsonNode jsonValue = node.get(jsonFieldName);

        if (jsonValue == null || jsonValue.isNull()) {
            return null;
        }

        if (fieldType == Long.class || fieldType == long.class) {
            return jsonValue.asLong();
        } else if (fieldType == String.class) {
            return jsonValue.asText();
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return jsonValue.asInt();
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(jsonValue.asText());
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return jsonValue.asBoolean();
        } else if (fieldType == LocalDate.class) {
            try {
                return convertDaysToDate(jsonValue.asInt()); // 숫자를 LocalDate로 변환
            } catch (Exception e) {
                logger.error("Failed to parse LocalDate from JSON field: {}", jsonFieldName, e);
            }
        } else {
            return jsonValue.asText();
        }
        return null;
    }

    // 숫자를 LocalDate로 변환하는 로직
    private static LocalDate convertDaysToDate(int days) {
        return EPOCH_DATE.plusDays(days); // 1970-01-01에서 days만큼 더한 날짜 반환
    }

    // Source 값과 Target 필드 타입 간의 변환 처리
    private static Object convertValue(Object sourceValue, Class<?> targetType) {
        if (sourceValue == null) {
            return null;
        }

        Class<?> sourceType = sourceValue.getClass();

        if (targetType == sourceType) {
            return sourceValue;
        }

        try {
            if (targetType == String.class) {
                if (sourceType == LocalDate.class) {
                    return ((LocalDate) sourceValue).toString();
                } else {
                    return sourceValue.toString();
                }
            } else if (targetType == LocalDate.class) {
                if (sourceType == String.class) {
                    return LocalDate.parse((String) sourceValue);
                }
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(sourceValue.toString());
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(sourceValue.toString());
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(sourceValue.toString());
            } else {
                return sourceValue.toString();
            }
        } catch (Exception e) {
            logger.error("Failed to convert value: {} to target type: {}", sourceValue, targetType, e);
        }

        return null;
    }
}
