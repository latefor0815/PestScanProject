package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 모든 이미지 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    /**
     * 이미지 ID로 이미지 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable Long id) {
        ImageDto image = imageService.getImageById(id);
        return ResponseEntity.ok(image);
    }

    /**
     * 이미지 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<ImageDto> uploadImage(@RequestParam("imageUrl") String imageUrl,
                                                @RequestParam("userId") Long userId) {
        ImageDto uploadedImage = imageService.uploadImage(imageUrl, userId);
        return ResponseEntity.status(201).body(uploadedImage);
    }
}