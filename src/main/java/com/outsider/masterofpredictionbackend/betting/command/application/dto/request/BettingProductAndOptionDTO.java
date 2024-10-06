package com.outsider.masterofpredictionbackend.betting.command.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BettingProductAndOptionDTO {

    private Long id;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    private Long userId;

    @NotNull(message = "categoryCode is required")
    private Long categoryCode;

    @Future(message = "deadLineDateTime must be future")
    private LocalDateTime deadLineDateTime;

    private LocalDate deadlineDate;

    private LocalTime deadlineTime;


    private List<MultipartFile> mainImgUrl;

    @NotNull(message = "isBlind is required")
    private Boolean isBlind;

    private String blindName;

    private List<BettingProductOptionDTO> options;

    public BettingProductAndOptionDTO(String title, String content, Long userId, Long categoryCode,
                                      LocalDateTime deadLineDateTime, LocalDate deadlineDate, LocalTime deadlineTime,
                                      List<MultipartFile> mainImgUrl, Boolean isBlind,
                                      String blindName, List<BettingProductOptionDTO> options) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.categoryCode = categoryCode;
        this.deadLineDateTime = deadLineDateTime;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.mainImgUrl = mainImgUrl;
        this.isBlind = isBlind;
        this.options = options;
        this.blindName = blindName;
    }
}
