package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.entity.Image;
import com.busanit501.pesttestproject0909.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    /**
     * 모든 이미지 목록 조회
     */
    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 ID로 이미지 조회
     */
    public ImageDto getImageById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return convertToDto(image.get());
        } else {
            throw new RuntimeException("Image not found with ID: " + id);
        }
    }

    /**
     * 이미지 업로드 처리
     */
    public ImageDto uploadImage(String imageUrl, Long userId) {
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setUserId(userId);
        Image savedImage = imageRepository.save(image);
        return convertToDto(savedImage);
    }

    // Entity -> DTO 변환
    private ImageDto convertToDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getImageUrl(),
                image.getUserId()
        );
    }
}