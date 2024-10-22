package com.outsider.masterofpredictionbackend.report.command.domain.repository;


import com.outsider.masterofpredictionbackend.report.command.domain.aggregate.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 필요한 추가 메서드가 있다면 정의합니다.
}
