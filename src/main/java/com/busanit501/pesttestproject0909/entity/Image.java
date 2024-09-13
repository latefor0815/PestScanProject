package com.busanit501.pesttestproject0909.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키 생성 전략 설정
    private Long id;

    private String fileName;  // 파일 이름을 저장할 필드
    private Long userId;  // 사용자 ID

    // Report와 일대다 관계 설정
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    // 기본 생성자
    public Image() {}

    // 생성자
    public Image(Long id, String fileName, Long userId) {
        this.id = id;
        this.fileName = fileName;
        this.userId = userId;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // reports 필드의 Getter 및 Setter
    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}