package com.outsider.masterofpredictionbackend.report.command.application.controller;

import com.outsider.masterofpredictionbackend.report.command.application.dto.ReportDTO;
import com.outsider.masterofpredictionbackend.report.command.application.service.ReportService;
import com.outsider.masterofpredictionbackend.report.command.domain.aggregate.Report;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<Report> createReport(@UserId CustomUserInfoDTO customUserInfoDTO, @RequestBody @Valid ReportDTO reportDto) {
        Report report = new Report();
        // DTO에서 엔티티로 데이터 복사
        report.setReporter(customUserInfoDTO.getUserId());
        report.setReportedUser(reportDto.getReportedUser());
        report.setReason(reportDto.getReason());
        report.setDetails(reportDto.getDetails());

        Report savedReport = reportService.saveReport(report);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Report>> getReports(
            @PageableDefault(size = 10, sort = "reportDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> reports = reportService.getAllReports(pageable);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    // 추가적인 엔드포인트가 필요하다면 작성
}
