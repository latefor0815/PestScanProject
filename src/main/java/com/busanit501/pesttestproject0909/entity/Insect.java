package com.busanit501.pesttestproject0909.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Insect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키 생성 전략
    private Long id;

    private String name;
    private String species;
    private String description;

    // Report와 일대다 관계 설정
    @OneToMany(mappedBy = "insect", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    // 기본 생성자
    public Insect() {}

    // 생성자
    public Insect(Long id, String name, String species, String description) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.description = description;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // reports 필드의 Getter 및 Setter
    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}