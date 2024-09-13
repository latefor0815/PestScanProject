package com.busanit501.pesttestproject0909.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // 이 부분을 수정
import java.time.LocalDateTime;

@Entity
public class Contact {

    @Id // jakarta.persistence.Id를 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String message;
    private LocalDateTime submittedAt;

    // 기본 생성자
    public Contact() {}

    // 생성자
    public Contact(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.submittedAt = LocalDateTime.now();
    }

    // Getter와 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}