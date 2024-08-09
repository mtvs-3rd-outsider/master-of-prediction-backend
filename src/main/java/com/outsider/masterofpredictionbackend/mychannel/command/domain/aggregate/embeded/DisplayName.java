package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DisplayName {
    @Column(name = "DISPLAY_NAME")
    private String value;

    protected DisplayName() {}

    public DisplayName(String value) {
        validate(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }
        if (value.length() > 50) { // Assuming a reasonable max length for a display name
            throw new IllegalArgumentException("Display name cannot exceed 50 characters");
        }
        if (value.length() < 3) { // Assuming a reasonable min length for a display name
            throw new IllegalArgumentException("Display name must be at least 3 characters long");
        }
    }

    @Override
    public String toString() {
        return "DisplayName{" +
                "value='" + value + '\'' +
                '}';
    }
}
