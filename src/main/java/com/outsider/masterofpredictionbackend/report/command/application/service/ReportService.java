package com.outsider.masterofpredictionbackend.report.command.application.service;

import com.outsider.masterofpredictionbackend.report.command.domain.aggregate.Report;
import com.outsider.masterofpredictionbackend.report.command.domain.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report saveReport(Report report) {
        report.setReportDate(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public Page<Report> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    // 기타 필요한 메서드 작성
}
