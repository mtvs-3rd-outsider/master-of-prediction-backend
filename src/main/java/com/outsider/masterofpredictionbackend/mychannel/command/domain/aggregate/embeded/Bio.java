package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Bio {
    @Column(name = "BIO")
    private String value;

    protected Bio() {}

    public Bio(String value) {
        validateBio(value);
        this.value = value;
    }

    private void validateBio(String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
//            throw new IllegalArgumentException("Bio cannot be empty");
        }
        if (value.length() > 160) {
            throw new IllegalArgumentException("Bio cannot exceed 160 characters");
        }
        if (value.length() < 10) {
//            throw new IllegalArgumentException("Bio must be at least 10 characters long");
            return;
        }

        String forbiddenCharacters = "<>|";
        for (char c : forbiddenCharacters.toCharArray()) {
            if (value.contains(String.valueOf(c))) {
                throw new IllegalArgumentException("Bio contains forbidden characters");
            }
        }

        String[] forbiddenWords = {"badword1", "badword2"};
        for (String word : forbiddenWords) {
            if (value.toLowerCase().contains(word)) {
                throw new IllegalArgumentException("Bio contains forbidden words");
            }
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Bio{" +
                "value='" + value + '\'' +
                '}';
    }
}
