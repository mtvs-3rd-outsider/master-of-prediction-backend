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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "users")
public class UserSearchModel {

    @Id
    @JsonProperty("user_id")
    private String userId;


    @JsonProperty("display_name")
    @Field(type = FieldType.Text)
    private String displayName;

    private String tier;

    @JsonProperty("user_img")
    private String userImg ;


    @JsonProperty("user_name")
    @Field(type = FieldType.Keyword)
    private String userName;

    // Getters and Setters
}
