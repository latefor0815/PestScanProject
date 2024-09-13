package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.service.ImageService;
import com.busanit501.pesttestproject0909.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReportService reportService;

    /**
     * 모든 이미지 목록 조회
     */
    @GetMapping
    public String getAllImages(Model model) {
        List<ImageDto> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "imageList"; // templates 폴더 내의 imageList.html 파일로 이동
    }

    /**
     * 사용자 ID로 이미지 목록 조회
     */
    @GetMapping("/user/{userId}")
    public String getImagesByUserId(@PathVariable Long userId, Model model) {
        List<ImageDto> images = imageService.getImagesByUserId(userId);
        model.addAttribute("images", images);
        return "imageListByUser"; // 사용자별 이미지 목록 페이지로 이동
    }

    /**
     * 이미지 ID로 이미지 조회
     */
    @GetMapping("/{id}")
    public String getImageById(@PathVariable Long id, Model model) {
        ImageDto image = imageService.getImageById(id);
        model.addAttribute("image", image);
        return "imageDetail"; // templates 폴더 내의 imageDetail.html 파일로 이동
    }

    /**
     * 이미지 업로드 폼 보여주기
     */
    @GetMapping("/upload")
    public String showUploadForm() {
        return "aiImage/upload"; // templates/aiImage/upload.html 파일로 이동
    }

    /**
     * 이미지 업로드 처리
     */
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              Model model,
                              HttpSession session) {
        try {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                logger.warn("User not logged in while attempting to upload image");
                return "redirect:/users/login";
            }

            ImageDto uploadedImage = imageService.uploadImage(file, loggedInUser.getId());
            model.addAttribute("uploadedImage", uploadedImage);

            // 리포트 생성 로직
            ReportDto createdReport = reportService.createReportForImage(loggedInUser.getId(), uploadedImage.getId());
            logger.info("Created report: {}", createdReport);

            model.addAttribute("message", "Image uploaded successfully and report created!");
        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            model.addAttribute("error", "Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during image upload", e);
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }

        return "redirect:/images/upload";
    }

    /**
     * 이미지 삭제
     */
    @PostMapping("/delete/{id}")
    public String deleteImage(@PathVariable Long id, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        try {
            boolean deleted = imageService.deleteImage(id, loggedInUser.getId());
            if (deleted) {
                model.addAttribute("message", "Image deleted successfully");
            } else {
                model.addAttribute("error", "Failed to delete image. It may not exist or you may not have permission.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting image", e);
            model.addAttribute("error", "An error occurred while deleting the image: " + e.getMessage());
        }

        return "redirect:/images";
    }
}