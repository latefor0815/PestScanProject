package com.busanit501.pesttestproject0909.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키 생성 전략 설정
    private Long id;

    private String imageUrl;
    private Long userId;  // 사용자 ID 추가

    // 기본 생성자
    public Image() {}

    // 생성자
    public Image(Long id, String imageUrl, Long userId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}