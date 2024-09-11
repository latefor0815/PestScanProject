package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ImageDto;
import com.busanit501.pesttestproject0909.service.ImageService;
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

    @Autowired
    private ImageService imageService;

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
                              @RequestParam("userId") Long userId,
                              Model model) {
        try {
            ImageDto uploadedImage = imageService.uploadImage(file, userId);
            model.addAttribute("uploadedImage", uploadedImage);
            model.addAttribute("message", "Image uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload image.");
        }

        // 업로드 성공 후 다시 업로드 페이지로 이동
        return "redirect:/images/upload";
    }
}