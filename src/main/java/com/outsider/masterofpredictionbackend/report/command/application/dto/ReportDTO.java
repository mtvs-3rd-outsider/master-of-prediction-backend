package com.outsider.masterofpredictionbackend.report.command.application.dto;

import com.google.firebase.database.annotations.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ReportDTO {
    @NotNull
    private Long reporter;
    @NotNull
    private Long reportedUser;

    @NotNull
    private String reason;

    @Size(max = 500)
    private String details;

    // Getter와 Setter
}
