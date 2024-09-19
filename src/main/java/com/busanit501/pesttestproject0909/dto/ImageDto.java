package com.busanit501.pesttestproject0909.dto;

import lombok.Data;

@Data
public class ImageDto {
    private String id;
    private String fileName;
    private String userId;
    private String predictedClassLabel;
    private double confidence;

    // 생성자, getter, setter 메서드는 @Data 어노테이션으로 자동 생성됩니다.
}