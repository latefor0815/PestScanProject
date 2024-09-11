package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.entity.Image;
import com.busanit501.pesttestproject0909.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    private final String uploadDirectory = "C:/upload/";  // 파일을 저장할 경로

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
     * 사용자 ID로 이미지 목록 조회
     */
    public List<ImageDto> getImagesByUserId(Long userId) {
        List<Image> images = imageRepository.findByUserId(userId);
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 업로드 처리
     */
    public ImageDto uploadImage(MultipartFile file, Long userId) throws IOException {
        String fileName = file.getOriginalFilename();  // 파일 이름 가져오기
        Path filePath = Paths.get(uploadDirectory + fileName);

        // 파일 저장
        Files.write(filePath, file.getBytes());

        // DB에 파일 이름 및 사용자 정보 저장
        Image image = new Image();
        image.setFileName(fileName);  // 파일 이름 저장
        image.setUserId(userId);
        Image savedImage = imageRepository.save(image);

        return convertToDto(savedImage);
    }

    // Entity -> DTO 변환
    private ImageDto convertToDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getFileName(),  // fileName을 반환
                image.getUserId()
        );
    }
}