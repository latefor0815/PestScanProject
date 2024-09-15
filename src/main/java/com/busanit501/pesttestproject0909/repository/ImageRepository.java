package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findByUserId(String userId);
    Optional<Image> findByFileName(String fileName);
    boolean existsByFileName(String fileName);
}