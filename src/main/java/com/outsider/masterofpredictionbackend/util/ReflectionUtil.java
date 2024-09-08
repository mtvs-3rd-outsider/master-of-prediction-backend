package com.outsider.masterofpredictionbackend.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import java.time.LocalDate;


public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);
    private static final LocalDate EPOCH_DATE = LocalDate.of(1970, 1, 1); // 1970-01-01 기준 날짜

    public static <S, T> T mapJsonNodeToEntity(JsonNode node, T targetEntity, Class<S> sourceClass) {
        Field[] targetFields = targetEntity.getClass().getDeclaredFields();
        Field[] sourceFields = sourceClass.getDeclaredFields();

        for (Field targetField : targetFields) {
            targetField.setAccessible(true); // private 필드 접근 가능하게 설정

            try {
                String jsonFieldName = getJsonFieldName(targetField); // 타겟 JSON 필드 이름 가져오기
                logger.info("Mapping field: {} to JSON field: {}", targetField.getName(), jsonFieldName); // 디버깅용 로그

                Field sourceField = getSourceFieldByColumn(sourceFields, jsonFieldName); // 원본 소스의 @Column과 매칭
                if (sourceField != null) {
                    sourceField.setAccessible(true);
                    Object sourceValue = getValueForField(node, sourceField, jsonFieldName);
                    logger.info("Source value for field {}: {}", jsonFieldName, sourceValue); // 디버깅용 로그

                    Object convertedValue = convertValue(sourceValue, targetField.getType());
                    logger.info("Converted value for field {}: {}", targetField.getName(), convertedValue); // 디버깅용 로그

                    if (convertedValue != null) {
                        targetField.set(targetEntity, convertedValue);
                    }
                } else {
                    logger.warn("JSON does not contain field: {}", jsonFieldName); // 필드가 없는 경우 경고 로그
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

    // 원본 소스 클래스에서 @Column을 기반으로 필드 찾기
    private static Field getSourceFieldByColumn(Field[] sourceFields, String jsonFieldName) {
        for (Field field : sourceFields) {
            Column column = field.getAnnotation(Column.class); // @Column 어노테이션 확인
            if (column != null && column.name().equalsIgnoreCase(jsonFieldName)) {
                return field; // @Column 이름과 jsonFieldName이 일치하면 필드를 반환
            }
        }

        // @Column 어노테이션이 없을 경우 필드 이름으로 찾기
        for (Field field : sourceFields) {
            if (field.getName().equalsIgnoreCase(jsonFieldName)) {
                return field; // 필드 이름이 jsonFieldName과 일치하면 필드를 반환
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
        } else if (fieldType == LocalDate.class) {
            try {
                return convertDaysToDate(jsonValue.asInt()); // 숫자를 LocalDate로 변환
            } catch (Exception e) {
                logger.error("Failed to parse LocalDate from JSON field: {}", jsonFieldName, e);
            }
        }else
        {
            return jsonValue.asText();
        }
        // 더 많은 타입에 대한 처리가 필요하다면 여기서 추가
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
            return sourceValue; // 같은 타입이면 변환 필요 없음
        }

        try {
            if (targetType == String.class) {
                if (sourceType == LocalDate.class) {
                    return ((LocalDate) sourceValue).toString(); // LocalDate -> String 변환
                } else {
                    return sourceValue.toString(); // 기본적으로 toString() 사용
                }
            } else if (targetType == LocalDate.class) {
                if (sourceType == String.class) {
                    return LocalDate.parse((String) sourceValue); // String -> LocalDate 변환
                }
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(sourceValue.toString());
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(sourceValue.toString());
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(sourceValue.toString());
            }else
            {
                return sourceValue.toString();
            }
            // 필요에 따라 더 많은 변환 로직 추가
        } catch (Exception e) {
            logger.error("Failed to convert value: {} to target type: {}", sourceValue, targetType, e);
        }

        return null; // 변환 실패 시 null 반환
    }
}
