package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // 사용자 ID로 이미지 목록 조회
    List<Image> findByUserId(Long userId);

    // 이미지 URL로 이미지 검색
    Optional<Image> findByImageUrl(String imageUrl);

    // 이미지 URL이 존재하는지 확인
    boolean existsByImageUrl(String imageUrl);
}