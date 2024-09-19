package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.dto.PredictionResponseDTO;
import com.busanit501.pesttestproject0909.entity.Image;
import com.busanit501.pesttestproject0909.repository.ImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ImageDto> getAllImages() {
        log.info("Fetching all images");
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ImageDto getImageById(String id) {
        log.info("Fetching image with ID: {}", id);
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return convertToDto(image.get());
        } else {
            log.warn("Image not found with ID: {}", id);
            throw new RuntimeException("Image not found with ID: " + id);
        }
    }

    public List<ImageDto> getImagesByUserId(String userId) {
        log.info("Fetching images for user ID: {}", userId);
        List<Image> images = imageRepository.findByUserId(userId);
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ImageDto uploadAndClassifyImage(MultipartFile file, String userId) throws IOException {
        log.info("Uploading and classifying image for user ID: {}", userId);

        // 이미지 저장
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setUserId(userId);
        image.setData(file.getBytes());
        Image savedImage = imageRepository.save(image);

        // Django API 호출
        PredictionResponseDTO predictionResponse = sendImageToDjangoServer(file.getBytes(), file.getOriginalFilename());

        // 분류 결과를 ImageDto에 포함
        ImageDto imageDto = convertToDto(savedImage);
        imageDto.setPredictedClassLabel(predictionResponse.getPredictedClassLabel());
        imageDto.setConfidence(predictionResponse.getConfidence());

        log.info("Image uploaded and classified: {}", savedImage.getId());
        return imageDto;
    }

    private PredictionResponseDTO sendImageToDjangoServer(byte[] imageBytes, String filename) throws IOException {
        // 이미지 파일을 MultipartBody로 구성
        RequestBody fileBody = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));

        // Multipart request body
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", filename, fileBody)
                .build();

        // Request 객체 생성
        Request request = new Request.Builder()
                .url("http://localhost:8000/api/classify/")
                .post(requestBody)
                .build();

        // 요청 실행
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 응답 바디를 String으로 읽기
            String responseBody = response.body().string();
            log.info("responseBody : " + responseBody);

            // 응답을 PredictionResponseDTO 객체로 변환
            return objectMapper.readValue(responseBody, PredictionResponseDTO.class);
        }
    }

    public boolean deleteImage(String id, String userId) {
        log.info("Attempting to delete image with ID: {} for user ID: {}", id, userId);
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent() && imageOpt.get().getUserId().equals(userId)) {
            imageRepository.delete(imageOpt.get());
            log.info("Image deleted from database: {}", id);
            return true;
        }
        log.warn("Image not found or user does not have permission. ID: {}, User ID: {}", id, userId);
        return false;
    }

    private ImageDto convertToDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setFileName(image.getFileName());
        dto.setUserId(image.getUserId());
        // 분류 결과는 여기서 설정하지 않음 (uploadAndClassifyImage에서 설정)
        return dto;
    }
}