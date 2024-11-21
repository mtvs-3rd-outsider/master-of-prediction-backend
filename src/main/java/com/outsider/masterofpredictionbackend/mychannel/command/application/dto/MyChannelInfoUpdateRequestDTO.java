package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyChannelInfoUpdateRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Size(max = 160, message = "Bio cannot exceed 160 characters")
    @Pattern(regexp = "^[^<>|]*$", message = "Bio contains forbidden characters")
    private String bio;

    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Website must be a valid URL"
    )
    @Size(max = 200, message = "Website URL cannot exceed 200 characters")
    private String website;

    @Size(max = 1000, message = "Banner image URL cannot exceed 1000 characters")
    private String bannerImg;
}
