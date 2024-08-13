package com.outsider.masterofpredictionbackend.betting.command.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
public class BettingProductAndOptionDTO {

    private Long id;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "categoryCode is required")
    private Long categoryCode;

    @Future(message = "deadlineDate must be future date")
    private LocalDate deadlineDate;

    @Future(message = "deadlineTime must be future date")
    private LocalTime deadlineTime;


    private List<MultipartFile> mainImgUrl;

    @NotNull(message = "isBlind is required")
    private Boolean isBlind;


    @NotNull(message = "option is required")
    @Valid
    private List<@Valid BettingProductOptionDTO> option;
}
