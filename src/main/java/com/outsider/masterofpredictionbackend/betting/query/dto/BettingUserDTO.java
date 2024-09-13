package com.outsider.masterofpredictionbackend.betting.query.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class BettingUserDTO {

    private Long userID;
    private String userName;
    private String displayName;
    private String tierName;
    private String userImg;

}
