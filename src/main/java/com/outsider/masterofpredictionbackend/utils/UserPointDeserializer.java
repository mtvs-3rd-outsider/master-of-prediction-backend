package com.outsider.masterofpredictionbackend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

public class UserPointDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String encoded = p.getText(); // Base64로 인코딩된 값
        try {
            // 빈 문자열 또는 "AA=="처럼 무효한 Base64 문자열 처리
            if (encoded == null || encoded.trim().isEmpty() || "AA==".equals(encoded)) {
                return BigDecimal.ZERO;  // 기본값을 0으로 설정
            }

            // Base64 디코딩 후 BigDecimal로 변환
            byte[] decodedBytes = Base64.getDecoder().decode(encoded);
            String decodedString = new String(decodedBytes);
            return new BigDecimal(decodedString);
        } catch (IllegalArgumentException e) {
            throw new JsonProcessingException("Invalid Base64 encoded value for user_point") {};
        }
    }
}
