package com.outsider.masterofpredictionbackend.mychannel.query.dto;


import lombok.Data;

import java.time.LocalDate;
@Data
public class MyChannelRegistRequestDTO {

    private String displayName;

    private String bio;

    private String location;

    private LocalDate joinDate;

    private String website;

    private Integer followersCount;

    private Integer followingCount;

    private Long user;

}
