package com.busanit501.pesttestproject0909.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportDto {
    private Long id;
    private Long userId;
    private String imageId;
    private String imageName;
    private String insectName;
    private String analysisResult;
    private String predictedClassLabel;
    private Double confidence;

    public ReportDto(Long id, Long userId, String imageId, String imageName, String insectName, String analysisResult, String predictedClassLabel, Double confidence) {
        this.id = id;
        this.userId = userId;
        this.imageId = imageId;
        this.imageName = imageName;
        this.insectName = insectName;
        this.analysisResult = analysisResult;
        this.predictedClassLabel = predictedClassLabel;
        this.confidence = confidence;
    }

    public String getFormattedAnalysisResult() {
        if (confidence != null && predictedClassLabel != null) {
            return String.format("%s (정확도: %.2f%%)", predictedClassLabel, confidence * 100);
        } else {
            return analysisResult;
        }
    }
}