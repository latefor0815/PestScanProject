package com.busanit501.pesttestproject0909.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultController {

    @GetMapping("/result")
    public String showResult(@RequestParam("imageId") String imageId,
                             @RequestParam("reportId") Long reportId,
                             Model model) {
        // 여기서 imageId와 reportId를 사용하여 필요한 데이터를 조회하고 모델에 추가합니다.
        // 예:
        // ImageDto image = imageService.getImageById(imageId);
        // ReportDto report = reportService.getReportById(reportId);
        // model.addAttribute("image", image);
        // model.addAttribute("report", report);

        return "aiImage/result";
    }
}