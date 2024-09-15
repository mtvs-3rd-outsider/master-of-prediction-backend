package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CommunityRule {

    @Column(name = "COMMUNITY_RULE", columnDefinition = "JSON")
    private String communityRule; // Stores the JSON string

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Constructor for a valid communityRule string
    public CommunityRule(String communityRule) {
        this.communityRule = communityRule; // Store the raw JSON string
    }

    // Default constructor for JPA
    public CommunityRule() {}

    public String getCommunityRule() {
        return communityRule;
    }

    public void setCommunityRule(String communityRule) {
        this.communityRule = communityRule;
    }

    // Method to return communityRule as JSON string (same as the stored JSON)
    public String toJson() {
        try {
            // In case communityRule is already a valid JSON string, just return it
            return MAPPER.writeValueAsString(MAPPER.readTree(communityRule));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for community rule", e);
        }
    }

    // Override equals and hashCode for proper comparisons
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityRule that = (CommunityRule) o;
        return communityRule.equals(that.communityRule);
    }

    @Override
    public int hashCode() {
        return communityRule.hashCode();
    }
}
