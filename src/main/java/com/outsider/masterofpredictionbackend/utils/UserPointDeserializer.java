package com.outsider.masterofpredictionbackend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

public class UserPointDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String encodedValue = p.getText();
        try {
            // Base64 디코딩
            byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            // Base64 디코딩 오류 처리
            throw new JsonMappingException(p, "Invalid Base64 encoded value for user_point", e);
        }
    }
}
