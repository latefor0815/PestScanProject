package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 특정 사용자의 모든 보고서를 조회하는 API
     * @param userId 사용자 ID
     * @return 사용자의 보고서 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDto>> getUserReports(@PathVariable Long userId) {
        List<ReportDto> reports = reportService.getReportsByUserId(userId);
        return ResponseEntity.ok(reports);
    }

    /**
     * 특정 보고서를 조회하는 API
     * @param reportId 보고서 ID
     * @return 보고서 정보
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long reportId) {
        ReportDto report = reportService.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    /**
     * 보고서를 생성하는 API
     * @param userId 사용자 ID
     * @param imageId 이미지 ID
     * @param analysisResult 분석 결과
     * @return 생성된 보고서 정보
     */
    @PostMapping("/create")
    public ResponseEntity<ReportDto> createReport(@RequestParam Long userId,
                                                  @RequestParam Long imageId,
                                                  @RequestParam String analysisResult) {
        ReportDto createdReport = reportService.saveReport(userId, imageId, analysisResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    /**
     * 보고서를 삭제하는 API
     * @param reportId 삭제할 보고서 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok("Report deleted successfully");
    }
}