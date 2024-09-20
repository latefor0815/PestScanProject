package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserId(Long userId);
    Page<Report> findByUserId(Long userId, Pageable pageable);
    Optional<Report> findByIdAndUserId(Long reportId, Long userId);
}