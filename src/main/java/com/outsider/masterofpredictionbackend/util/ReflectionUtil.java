package com.outsider.masterofpredictionbackend.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷 정의

    public static <S, T> T mapJsonNodeToEntity(JsonNode node, T targetEntity, Class<S> sourceClass) {
        Field[] targetFields = targetEntity.getClass().getDeclaredFields();
        Field[] sourceFields = sourceClass.getDeclaredFields();

        for (Field targetField : targetFields) {
            targetField.setAccessible(true); // private 필드 접근 가능하게 설정

            try {
                String jsonFieldName = getJsonFieldName(targetField); // JSON 필드 이름 가져오기

                if (node.has(jsonFieldName)) {
                    Field sourceField = getSourceFieldByName(sourceFields, targetField.getName());
                    if (sourceField != null) {
                        sourceField.setAccessible(true);
                        Object sourceValue = getValueForField(node, sourceField, jsonFieldName);
                        Object convertedValue = convertValue(sourceValue, targetField.getType());
                        targetField.set(targetEntity, convertedValue);
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("Failed to set field value for field: {}", targetField.getName(), e);
            }
        }
        return targetEntity;
    }

    // 필드에서 JSON 필드 이름 가져오기 (snake_case 혹은 camelCase와 매핑)
    private static String getJsonFieldName(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value(); // @JsonProperty가 있으면 그 값을 사용
        }
        return field.getName(); // 없으면 필드 이름을 그대로 사용
    }

    // Source 필드 이름을 기반으로 Source 클래스에서 해당 필드 찾기
    private static Field getSourceFieldByName(Field[] sourceFields, String fieldName) {
        for (Field field : sourceFields) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return field;
            }
        }
        return null; // 필드가 없으면 null 반환
    }

    // JSON 노드에서 값 추출 (Source 필드의 타입에 맞게 변환)
    private static Object getValueForField(JsonNode node, Field field, String jsonFieldName) {
        Class<?> fieldType = field.getType();
        JsonNode jsonValue = node.get(jsonFieldName);

        if (jsonValue == null || jsonValue.isNull()) {
            return null; // 값이 없으면 null 반환
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
        } else if (fieldType == Date.class) {
            try {
                return new Date(jsonValue.asLong()); // Timestamp 값을 Date로 변환
            } catch (Exception e) {
                logger.error("Failed to parse date from JSON field: {}", jsonFieldName, e);
            }
        }
        // 더 많은 타입에 대한 처리가 필요하다면 여기서 추가
        return null;
    }

    // Source 값과 Target 필드 타입 간의 변환 처리
    private static Object convertValue(Object sourceValue, Class<?> targetType) {
        if (sourceValue == null) {
            return null;
        }

        Class<?> sourceType = sourceValue.getClass();

        if (targetType == sourceType) {
            return sourceValue; // 같은 타입이면 변환 필요 없음
        }

        try {
            if (targetType == String.class) {
                if (sourceType == Date.class) {
                    return dateFormat.format((Date) sourceValue); // Date -> String 변환
                } else {
                    return sourceValue.toString(); // 기본적으로 toString() 사용
                }
            } else if (targetType == Date.class) {
                if (sourceType == String.class) {
                    return dateFormat.parse((String) sourceValue); // String -> Date 변환
                }
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(sourceValue.toString());
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(sourceValue.toString());
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(sourceValue.toString());
            }
            // 필요에 따라 더 많은 변환 로직 추가
        } catch (Exception e) {
            logger.error("Failed to convert value: {} to target type: {}", sourceValue, targetType, e);
        }

        return null; // 변환 실패 시 null 반환
    }
}