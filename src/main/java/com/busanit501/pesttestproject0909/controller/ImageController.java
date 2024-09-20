package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.service.ImageService;
import com.busanit501.pesttestproject0909.service.ReportService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/images")
@Log4j2
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/upload")
    public ModelAndView showUploadForm() {
        return new ModelAndView("aiImage/upload");
    }

    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ImageDto>> getImagesByUserId(@PathVariable Long userId) {
        List<ImageDto> images = imageService.getImagesByUserId(userId.toString());
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable String id) {
        try {
            ImageDto image = imageService.getImageById(id);
            return ResponseEntity.ok(image);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        try {
            ImageDto uploadedImage = imageService.uploadAndClassifyImage(file, loggedInUser.getId().toString());
            // 수정된 부분: ReportService.createReportForImage 메소드 호출 시 인자 추가
            ReportDto createdReport = reportService.createReportForImage(
                    loggedInUser.getId(),
                    uploadedImage.getId(),
                    uploadedImage.getPredictedClassLabel(),
                    uploadedImage.getConfidence()
            );

            String redirectUrl = "/result?imageId=" + uploadedImage.getId() + "&reportId=" + createdReport.getId();
            return ResponseEntity.ok().body(Map.of("redirect", redirectUrl));
        } catch (Exception e) {
            log.error("An error occurred during image upload and processing", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable String id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        boolean deleted = imageService.deleteImage(id, loggedInUser.getId().toString());
        if (deleted) {
            return ResponseEntity.ok("Image deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Failed to delete image. It may not exist or you may not have permission.");
        }
    }
}