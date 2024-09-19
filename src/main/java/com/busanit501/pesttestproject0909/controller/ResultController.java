package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.service.ImageService;
import com.busanit501.pesttestproject0909.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.HashMap;

@Controller
public class ResultController {

    private static final Logger logger = LoggerFactory.getLogger(ResultController.class);

    private final ImageService imageService;
    private final ReportService reportService;
    private final Map<String, String> translationMap;

    @Autowired
    public ResultController(ImageService imageService, ReportService reportService) {
        this.imageService = imageService;
        this.reportService = reportService;
        this.translationMap = initializeTranslationMap();
    }

    @GetMapping("/result")
    public String showResult(@RequestParam("imageId") String imageId,
                             @RequestParam("reportId") Long reportId,
                             Model model) {
        try {
            logger.info("Fetching result for imageId: {} and reportId: {}", imageId, reportId);

            ImageDto image = imageService.getImageById(imageId);
            ReportDto report = reportService.getReportById(reportId);

            String predictedClassLabel = image.getPredictedClassLabel();
            String koreanLabel = (predictedClassLabel != null && !predictedClassLabel.isEmpty())
                    ? translateToKorean(predictedClassLabel)
                    : "알 수 없음";

            double confidence = image.getConfidence() * 100;

            model.addAttribute("koreanLabel", koreanLabel);
            model.addAttribute("confidence", String.format("%.1f", confidence));
            model.addAttribute("imageName", image.getFileName());
            model.addAttribute("reportAnalysis", report.getAnalysisResult());

            logger.info("Result retrieved successfully. Korean label: {}, Confidence: {}", koreanLabel, confidence);

            return "aiImage/result";
        } catch (Exception e) {
            logger.error("Error occurred while fetching result", e);
            model.addAttribute("error", "결과를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }

    private String translateToKorean(String englishLabel) {
        return translationMap.getOrDefault(englishLabel.toLowerCase(), "알 수 없는 해충");
    }

    private Map<String, String> initializeTranslationMap() {
        Map<String, String> map = new HashMap<>();
        map.put("aphid", "진딧물");
        map.put("caelifera", "메뚜기");
        map.put("armyworm", "옥수수에벌래");
        map.put("politus", "벼멸구");
        map.put("potatobeetle", "감자잎벌레");
        map.put("tephritidae", "과실파리");
        // 필요에 따라 더 많은 번역을 추가할 수 있습니다.
        return map;
    }
}