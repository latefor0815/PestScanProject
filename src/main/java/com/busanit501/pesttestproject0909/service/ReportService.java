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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<ReportDto> getReportsPaginated(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Report> reportPage = reportRepository.findByUserId(userId, pageable);
        return reportPage.map(this::toReportDto);
    }

    public List<ReportDto> getReportsByUserId(Long userId) {
        logger.info("Fetching reports for user ID: {}", userId);
        List<Report> reports = reportRepository.findByUserId(userId);
        logger.info("Found {} reports in the database", reports.size());

        List<ReportDto> reportDtos = reports.stream()
                .map(this::toReportDto)
                .collect(Collectors.toList());

        logger.info("Converted {} reports to DTOs", reportDtos.size());
        return reportDtos;
    }

    public ReportDto getReportById(Long reportId) {
        logger.info("Fetching report with ID: {}", reportId);
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));
        logger.info("Found report: {}", report);
        return toReportDto(report);
    }

    public ReportDto getReportByIdAndUserId(Long reportId, Long userId) {
        logger.info("Fetching report with ID: {} for user ID: {}", reportId, userId);
        Report report = reportRepository.findByIdAndUserId(reportId, userId)
                .orElse(null);
        if (report != null) {
            logger.info("Found report: {}", report);
            return toReportDto(report);
        } else {
            logger.warn("Report not found for ID: {} and user ID: {}", reportId, userId);
            return null;
        }
    }

    @Transactional
    public ReportDto saveReport(Long userId, String imageId, String predictedClassLabel, Double confidence) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Report report = new Report();
        report.setUser(user);
        report.setImageId(imageId);
        report.setPredictedClassLabel(predictedClassLabel);
        report.setConfidence(confidence);

        Report savedReport = reportRepository.save(report);
        logger.info("Saved report: {}", savedReport);
        return toReportDto(savedReport);
    }

    @Transactional
    public boolean deleteReport(Long reportId, Long userId) {
        logger.info("Attempting to delete report with ID: {} for user ID: {}", reportId, userId);
        return reportRepository.findByIdAndUserId(reportId, userId)
                .map(report -> {
                    reportRepository.delete(report);
                    logger.info("Deleted report with ID: {}", reportId);
                    return true;
                })
                .orElseGet(() -> {
                    logger.warn("Report not found for deletion. ID: {}, User ID: {}", reportId, userId);
                    return false;
                });
    }

    @Transactional
    public ReportDto createReportForImage(Long userId, String imageId, String predictedClassLabel, Double confidence) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Insect insect = insectRepository.findByName(predictedClassLabel)
                .orElseGet(() -> {
                    Insect newInsect = new Insect();
                    newInsect.setName(predictedClassLabel);
                    return insectRepository.save(newInsect);
                });

        Report report = new Report();
        report.setUser(user);
        report.setImageId(imageId);
        report.setInsect(insect);
        report.setPredictedClassLabel(predictedClassLabel);
        report.setConfidence(confidence);

        String analysisResult = String.format("%s (정확도: %.2f%%)", predictedClassLabel, confidence * 100);
        report.setAnalysisResult(analysisResult);

        Report savedReport = reportRepository.save(report);
        logger.info("Saved new report: {}", savedReport);

        return toReportDto(savedReport);
    }

    private ReportDto toReportDto(Report report) {
        User user = report.getUser();
        Image image = imageRepository.findById(report.getImageId()).orElse(null);
        Insect insect = report.getInsect();

        return new ReportDto(
                report.getId(),
                user != null ? user.getId() : null,
                report.getImageId(),
                image != null ? image.getFileName() : null,
                insect != null ? insect.getName() : null,
                report.getAnalysisResult(),
                report.getPredictedClassLabel(),
                report.getConfidence()
        );
    }
}