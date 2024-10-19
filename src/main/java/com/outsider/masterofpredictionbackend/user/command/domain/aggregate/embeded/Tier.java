package com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Tier {

    @Column(name = "tier_name")
    private String name;

    @Column(name = "tier_level")
    private int level; // 1 to 5, applicable for SEER, NOVICE, ORACLE, PROPHET

    // Constructors
    public Tier() {
        this.name = "UNRANKED";
        this.level = 0;
    }

    public Tier(String name, int level) {
        this.name = name;
        this.level = level;
    }

    // Factory method to determine tier based on percentile and betting activity
    public static Tier getTierByPercentileAndBets(double percentile) {
        int minimumBetsRequired = 1;
        if (percentile < 0 || percentile > 100 ) {
            throw new IllegalArgumentException("Percentile must be between 0 and 100, and number of bets cannot be negative");
        }

        // Check if the user has made the minimum required bets, otherwise they are unranked
//        if (numberOfBets < minimumBetsRequired) {
//            return new Tier("UNRANKED", 0);
//        }

        // Determine the tier based on percentile if the betting requirement is met
        if (percentile >= 99) {
            return new Tier("NOSTRADAMUS", 1);
        } else if (percentile >= 95) {
            return new Tier("PROPHET", 5);
        } else if (percentile >= 90) {
            return new Tier("PROPHET", 4);
        } else if (percentile >= 85) {
            return new Tier("PROPHET", 3);
        } else if (percentile >= 80) {
            return new Tier("PROPHET", 2);
        } else if (percentile >= 75) {
            return new Tier("PROPHET", 1);
        } else if (percentile >= 70) {
            return new Tier("ORACLE", 5);
        } else if (percentile >= 65) {
            return new Tier("ORACLE", 4);
        } else if (percentile >= 60) {
            return new Tier("ORACLE", 3);
        } else if (percentile >= 55) {
            return new Tier("ORACLE", 2);
        } else if (percentile >= 50) {
            return new Tier("ORACLE", 1);
        } else if (percentile >= 45) {
            return new Tier("NOVICE", 5);
        } else if (percentile >= 40) {
            return new Tier("NOVICE", 4);
        } else if (percentile >= 35) {
            return new Tier("NOVICE", 3);
        } else if (percentile >= 30) {
            return new Tier("NOVICE", 2);
        } else if (percentile >= 25) {
            return new Tier("NOVICE", 1);
        } else if (percentile >= 20) {
            return new Tier("SEER", 5);
        } else if (percentile >= 15) {
            return new Tier("SEER", 4);
        } else if (percentile >= 10) {
            return new Tier("SEER", 3);
        } else if (percentile >= 5) {
            return new Tier("SEER", 2);
        } else {
            return new Tier("SEER", 1);
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return name + " Level " + level;
    }

    // equals, hashCode 메소드 구현 필요
}
