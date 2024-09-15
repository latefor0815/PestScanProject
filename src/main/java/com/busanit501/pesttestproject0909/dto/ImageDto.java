package com.busanit501.pesttestproject0909.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String id;
    private String fileName;  // 파일 이름
    private String userId;  // 이미지를 업로드한 사용자 ID
}