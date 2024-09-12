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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private InsectRepository insectRepository;

    public List<ReportDto> getReportsByUserId(Long userId) {
        List<Report> reports = reportRepository.findByUserId(userId);
        return reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReportDto getReportByIdAndUserId(Long reportId, Long userId) {
        Report report = reportRepository.findByIdAndUserId(reportId, userId)
                .orElse(null);
        return (report != null) ? convertToDto(report) : null;
    }

    @Transactional
    public ReportDto saveReport(Long userId, Long imageId, Long insectId, String analysisResult) {
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

        return convertToDto(savedReport);
    }

    @Transactional
    public boolean deleteReport(Long reportId, Long userId) {
        Report report = reportRepository.findByIdAndUserId(reportId, userId)
                .orElse(null);
        if (report != null) {
            reportRepository.delete(report);
            return true;
        }
        return false;
    }

    private ReportDto convertToDto(Report report) {
        return new ReportDto(
                report.getId(),
                report.getUser().getId(),
                report.getImage().getFileName(),
                report.getInsect().getName(),
                report.getAnalysisResult()
        );
    }
}