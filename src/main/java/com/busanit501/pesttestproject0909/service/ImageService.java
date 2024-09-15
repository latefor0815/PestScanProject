package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.entity.Image;
import com.busanit501.pesttestproject0909.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    private ImageRepository imageRepository;

    public List<ImageDto> getAllImages() {
        logger.info("Fetching all images");
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ImageDto getImageById(String id) {
        logger.info("Fetching image with ID: {}", id);
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return convertToDto(image.get());
        } else {
            logger.warn("Image not found with ID: {}", id);
            throw new RuntimeException("Image not found with ID: " + id);
        }
    }

    public List<ImageDto> getImagesByUserId(String userId) {
        logger.info("Fetching images for user ID: {}", userId);
        List<Image> images = imageRepository.findByUserId(userId);
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ImageDto uploadImage(MultipartFile file, String userId) throws IOException {
        logger.info("Uploading image for user ID: {}", userId);
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setUserId(userId);
        image.setData(file.getBytes());
        Image savedImage = imageRepository.save(image);
        logger.info("Image saved to database: {}", savedImage.getId());
        return convertToDto(savedImage);
    }

    public boolean deleteImage(String id, String userId) {
        logger.info("Attempting to delete image with ID: {} for user ID: {}", id, userId);
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent() && imageOpt.get().getUserId().equals(userId)) {
            imageRepository.delete(imageOpt.get());
            logger.info("Image deleted from database: {}", id);
            return true;
        }
        logger.warn("Image not found or user does not have permission. ID: {}, User ID: {}", id, userId);
        return false;
    }

    private ImageDto convertToDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getFileName(),
                image.getUserId()
        );
    }
}