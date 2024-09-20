package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @GetMapping({"/list", "/my-reports"})
    public String listReports(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "15") int size,
                              Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warn("User not logged in, redirecting to login page");
            return "redirect:/users/login";
        }

        logger.info("Logged in user ID: {}", loggedInUser.getId());

        try {
            Page<ReportDto> reportPage = reportService.getReportsPaginated(loggedInUser.getId(), page, size);
            logger.info("Retrieved {} reports for user ID: {}", reportPage.getContent().size(), loggedInUser.getId());

            for (ReportDto report : reportPage.getContent()) {
                logger.info("Report: {}, Analysis Result: {}", report, report.getFormattedAnalysisResult());
            }

            model.addAttribute("reports", reportPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", reportPage.getTotalPages());
            model.addAttribute("totalItems", reportPage.getTotalElements());

            if (reportPage.isEmpty()) {
                logger.info("No reports found for user");
                model.addAttribute("message", "현재 등록된 리포트가 없습니다.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching reports", e);
            model.addAttribute("error", "리포트 조회 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "report";
    }

    @GetMapping("/{reportId}")
    public String getReportById(@PathVariable Long reportId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        try {
            ReportDto report = reportService.getReportByIdAndUserId(reportId, loggedInUser.getId());
            if (report != null) {
                model.addAttribute("report", report);
            } else {
                model.addAttribute("error", "리포트를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            logger.error("리포트 조회 중 오류 발생", e);
            model.addAttribute("error", "리포트 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "reportDetail";
    }

    @PostMapping("/create")
    public String createReport(@RequestParam String imageId,
                               @RequestParam String predictedClassLabel,
                               @RequestParam Double confidence,
                               HttpSession session,
                               Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        try {
            ReportDto createdReport = reportService.createReportForImage(loggedInUser.getId(), imageId, predictedClassLabel, confidence);
            logger.info("새 리포트 생성: {}", createdReport);
            return "redirect:/reports/list";
        } catch (Exception e) {
            logger.error("리포트 생성 중 오류 발생", e);
            model.addAttribute("error", "리포트 생성 중 오류가 발생했습니다: " + e.getMessage());
            return "report";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        boolean isDeleted = reportService.deleteReport(id, loggedInUser.getId());
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "리포트가 성공적으로 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "리포트 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/reports/list";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("예외 발생", e);
        model.addAttribute("error", "예기치 못한 오류가 발생했습니다: " + e.getMessage());
        return "report";
    }
}