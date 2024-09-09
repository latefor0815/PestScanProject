package com.busanit501.pesttestproject0909.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long reportId;
    private String insectName;
    private String analysisResult;
}