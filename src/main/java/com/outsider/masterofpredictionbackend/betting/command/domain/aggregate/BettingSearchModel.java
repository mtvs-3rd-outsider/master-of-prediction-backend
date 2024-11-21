package com.outsider.masterofpredictionbackend.betting.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "betting_products")
public class BettingSearchModel {
    @Id
    @JsonProperty("id")
    @Field(type = FieldType.Long)
    private Long bettingId;

    @JsonProperty("content")
    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)  // 필드 추가
    @JsonProperty("title")
    private String title;
}