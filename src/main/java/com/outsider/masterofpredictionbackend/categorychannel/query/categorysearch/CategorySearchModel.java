package com.outsider.masterofpredictionbackend.categorychannel.query.categorysearch;

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
@Document(indexName = "categories")
public class CategorySearchModel {


    @Id
    @JsonProperty("category_channel_id")
    @Field(type = FieldType.Long)
    private Long categoryChannelId;

    @JsonProperty("display_name")
    @Field(type = FieldType.Text)
    private String displayName;



    @JsonProperty("image_url")
    @Field(type = FieldType.Text)  // text 타입으로 설정
    private String categoryImg ;



    // Getters and Setters
}
