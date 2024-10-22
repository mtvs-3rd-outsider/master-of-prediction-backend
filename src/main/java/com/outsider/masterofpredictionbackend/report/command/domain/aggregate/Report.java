package com.outsider.masterofpredictionbackend.report.command.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reporter; // 신고자
    private Long reportedUser; // 신고 대상자
    private String reason; // 신고 사유
    private String details; // 상세 내용
    private LocalDateTime reportDate;

    // Getter와 Setter 추가

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReporter() {
        return reporter;
    }

    public void setReporter(Long reporter) {
        this.reporter = reporter;
    }

    public Long getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Long reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }
}

