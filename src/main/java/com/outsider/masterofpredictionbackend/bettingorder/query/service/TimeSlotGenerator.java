package com.outsider.masterofpredictionbackend.bettingorder.query.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotGenerator {

    public static List<LocalDateTime> generateMinuteIntervals(LocalDateTime endTime, int startMinutesAgo, int intervalMinutes) {
        List<LocalDateTime> timeSlots = new ArrayList<>();
        LocalDateTime startTime = endTime.minusMinutes(startMinutesAgo);


        while (!startTime.isAfter(endTime)) {
            timeSlots.add(startTime.withNano(0));
            startTime = startTime.plusMinutes(intervalMinutes);
        }

        return timeSlots;
    }
}
