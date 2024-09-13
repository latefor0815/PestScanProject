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
        return reportRepository.findByIdAndUserId(reportId, userId)
                .map(report -> {
                    // 관련된 엔티티들의 연관관계 처리
                    Image image = report.getImage();
                    Insect insect = report.getInsect();
                    User user = report.getUser();

                    if (image != null) {
                        image.getReports().remove(report);
                    }
                    if (insect != null) {
                        insect.getReports().remove(report);
                    }
                    if (user != null) {
                        user.getReports().remove(report);
                    }

                    // 실제로 데이터베이스에서 리포트 삭제
                    reportRepository.delete(report);
                    logger.info("Deleted report with ID: {}", reportId);

                    // 연관된 이미지도 삭제 (필요한 경우)
                    if (image != null && image.getReports().isEmpty()) {
                        imageRepository.delete(image);
                        logger.info("Deleted associated image with ID: {}", image.getId());
                    }

                    return true;
                })
                .orElseGet(() -> {
                    logger.warn("Report not found for deletion. ID: {}, User ID: {}", reportId, userId);
                    return false;
                });
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
                .orElseGet(() -> {
                    logger.warn("Default insect not found. Creating a new one.");
                    Insect newInsect = new Insect();
                    newInsect.setName("Unknown Insect");
                    newInsect.setSpecies("Unknown Species");
                    return insectRepository.save(newInsect);
                });

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