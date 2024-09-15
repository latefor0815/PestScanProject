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

    @GetMapping
    public String getAllImages(Model model) {
        List<ImageDto> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "imageList";
    }

    @GetMapping("/user/{userId}")
    public String getImagesByUserId(@PathVariable String userId, Model model) {
        List<ImageDto> images = imageService.getImagesByUserId(userId);
        model.addAttribute("images", images);
        return "imageListByUser";
    }

    @GetMapping("/{id}")
    public String getImageById(@PathVariable String id, Model model) {
        ImageDto image = imageService.getImageById(id);
        model.addAttribute("image", image);
        return "imageDetail";
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "aiImage/upload";
    }

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

            ImageDto uploadedImage = imageService.uploadImage(file, loggedInUser.getId().toString());
            model.addAttribute("uploadedImage", uploadedImage);

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

    @PostMapping("/delete/{id}")
    public String deleteImage(@PathVariable String id, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        try {
            boolean deleted = imageService.deleteImage(id, loggedInUser.getId().toString());
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