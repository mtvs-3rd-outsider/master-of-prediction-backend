package com.outsider.masterofpredictionbackend.user.query.usersearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "users")
public class UserSearchModel {


    @Id
    @JsonProperty("user_id")
    @Field(type = FieldType.Long)
    private Long userId;

    @JsonProperty("display_name")
    @Field(type = FieldType.Text)
    private String displayName;

    @Field(type = FieldType.Keyword)  // 필드 추가
    @JsonProperty("tier_name")
    private String tier;

    @JsonProperty("user_img")
    @Field(type = FieldType.Text)  // text 타입으로 설정
    private String userImg ;


    @JsonProperty("user_name")
    @Field(type = FieldType.Keyword)
    private String userName;

    // Getters and Setters
}
