package com.busanit501.pesttestproject0909.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "image_id")
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "insect_id", nullable = false)
    private Insect insect;

    @Column
    private String analysisResult;

    @Column
    private String predictedClassLabel;

    @Column
    private Double confidence;
}