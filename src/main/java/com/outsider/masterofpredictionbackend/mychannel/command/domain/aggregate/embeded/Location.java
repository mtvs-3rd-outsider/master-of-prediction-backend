package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    @Column(name = "LOCATION")
    private String value;

    protected Location() {}

    public Location(String value) {
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
        if (value != null && value.length() > 100) {
            throw new IllegalArgumentException("Location cannot exceed 100 characters");
        }
    }

    @Override
    public String toString() {
        return "Location{" +
                "value='" + value + '\'' +
                '}';
    }
}
