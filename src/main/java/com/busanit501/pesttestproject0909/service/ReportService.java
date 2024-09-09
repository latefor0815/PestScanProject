package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.entity.Image;
import com.busanit501.pesttestproject0909.entity.Report;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.repository.ImageRepository;
import com.busanit501.pesttestproject0909.repository.ReportRepository;
import com.busanit501.pesttestproject0909.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    /**
     * 분석 결과를 저장하는 메서드
     */
    public ReportDto saveReport(Long userId, Long imageId, String analysisResult) {
        // 사용자와 이미지 데이터 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Report 객체 생성 및 저장
        Report report = new Report();
        report.setUser(user);
        report.setImage(image);
        report.setAnalysisResult(analysisResult);
        Report savedReport = reportRepository.save(report);

        // 저장된 보고서를 DTO로 반환
        return new ReportDto(savedReport.getId(), image.getImageUrl(), analysisResult);
    }

    /**
     * 사용자별 보고서 목록 조회
     */
    public List<ReportDto> getReportsByUserId(Long userId) {
        List<Report> reports = reportRepository.findByUserId(userId);
        return reports.stream()
                .map(report -> new ReportDto(report.getId(), report.getImage().getImageUrl(), report.getAnalysisResult()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 보고서 조회
     */
    public ReportDto getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return new ReportDto(report.getId(), report.getImage().getImageUrl(), report.getAnalysisResult());
    }

    /**
     * 보고서 삭제
     */
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        reportRepository.delete(report);
    }
}