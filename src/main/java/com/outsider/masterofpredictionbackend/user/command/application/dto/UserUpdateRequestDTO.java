package com.outsider.masterofpredictionbackend.user.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class UserUpdateRequestDTO {
    private Long userId;
    private String displayName;
    private String userName;
    private Integer age;
    private Gender gender;
    private Location location;
    private LocalDate birthday;
    private String avatarUrl;
}
