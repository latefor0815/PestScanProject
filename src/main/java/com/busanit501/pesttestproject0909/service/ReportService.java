package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.entity.Image;
import com.busanit501.pesttestproject0909.entity.Insect;
import com.busanit501.pesttestproject0909.entity.Report;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.repository.ImageRepository;
import com.busanit501.pesttestproject0909.repository.InsectRepository;
import com.busanit501.pesttestproject0909.repository.ReportRepository;
import com.busanit501.pesttestproject0909.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private InsectRepository insectRepository;

    public List<ReportDto> getReportsByUserId(Long userId) {
        logger.info("Fetching reports for user ID: {}", userId);
        List<Report> reports = reportRepository.findByUserId(userId);
        logger.info("Found {} reports in the database", reports.size());

        List<ReportDto> reportDtos = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        logger.info("Converted {} reports to DTOs", reportDtos.size());
        return reportDtos;
    }

    public ReportDto getReportByIdAndUserId(Long reportId, Long userId) {
        logger.info("Fetching report with ID: {} for user ID: {}", reportId, userId);
        Report report = reportRepository.findByIdAndUserId(reportId, userId)
                .orElse(null);
        if (report != null) {
            logger.info("Found report: {}", report);
            return convertToDto(report);
        } else {
            logger.warn("Report not found for ID: {} and user ID: {}", reportId, userId);
            return null;
        }
    }

    @Transactional
    public ReportDto saveReport(Long userId, Long imageId, Long insectId, String analysisResult) {
        logger.info("Saving report for user ID: {}, image ID: {}, insect ID: {}", userId, imageId, insectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        Insect insect = insectRepository.findById(insectId)
                .orElseThrow(() -> new RuntimeException("Insect not found"));

        Report report = new Report();
        report.setUser(user);
        report.setImage(image);
        report.setInsect(insect);
        report.setAnalysisResult(analysisResult);
        Report savedReport = reportRepository.save(report);

        logger.info("Saved report: {}", savedReport);
        return convertToDto(savedReport);
    }

    @Transactional
    public boolean deleteReport(Long reportId, Long userId) {
        logger.info("Attempting to delete report with ID: {} for user ID: {}", reportId, userId);
        Report report = reportRepository.findByIdAndUserId(reportId, userId)
                .orElse(null);
        if (report != null) {
            reportRepository.delete(report);
            logger.info("Deleted report with ID: {}", reportId);
            return true;
        }
        logger.warn("Report not found for deletion. ID: {}, User ID: {}", reportId, userId);
        return false;
    }

    private ReportDto convertToDto(Report report) {
        ReportDto dto = new ReportDto(
                report.getId(),
                report.getUser().getId(),
                report.getImage().getFileName(),
                report.getInsect().getName(),
                report.getAnalysisResult()
        );
        logger.debug("Converted report to DTO: {}", dto);
        return dto;
    }

    @Transactional
    public ReportDto createReportForImage(Long userId, Long imageId) {
        logger.info("Creating report for image. User ID: {}, Image ID: {}", userId, imageId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // 기본 곤충 정보 사용 (나중에 AI 분석 결과로 대체)
        Insect defaultInsect = insectRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default insect not found"));

        Report report = new Report();
        report.setUser(user);
        report.setImage(image);
        report.setInsect(defaultInsect);
        report.setAnalysisResult("기본 분석 결과");

        Report savedReport = reportRepository.save(report);
        logger.info("Saved new report: {}", savedReport);

        return convertToDto(savedReport);
    }
}