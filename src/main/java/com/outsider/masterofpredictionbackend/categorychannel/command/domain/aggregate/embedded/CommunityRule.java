package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

@Embeddable
public class CommunityRule {

    @Column(name = "COMMUNITY_RULE")
    private String communityRule;

    @Transient
    private JsonNode communityRuleJsonNode;

    public CommunityRule(String communityRule) {
        validateCommunityRule(communityRule);
        this.communityRule = communityRule;
    }

    public CommunityRule() {}

    public String getCommunityRule() {
        return communityRule;
    }

    public JsonNode getCommunityRuleJsonNode() {
        return communityRuleJsonNode;
    }
    /* 예상되는 json 형식 (규칙 숫자는 가변)
        {
          "rules": [
            {
              "number": 1,
              "rule": "규칙 1"
            },
            {
              "number": 2,
              "rule": "규칙 2"
            }
          ]
        }
     */
    private void validateCommunityRule(String communityRule) {
        // json parsing
        communityRuleJsonNode = getCommunityRuleJsonNode(communityRule);

        // 입력값 중 규칙 하나는 있는지 확인
        // 규칙 최대갯수 제한 여부는 아직 미정
    }

    @Transient
    private JsonNode getCommunityRuleJsonNode(String communityRule) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(communityRule);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transient
    private void setCommunityRuleString(JsonNode communityRuleJsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            communityRule = mapper.writeValueAsString(communityRuleJsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
