package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ReportDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping({"/list", "/my-reports"})
    public String listReports(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        List<ReportDto> reports = reportService.getReportsByUserId(loggedInUser.getId());
        model.addAttribute("reports", reports);
        return "report";
    }

    @PostMapping("/create")
    public ResponseEntity<ReportDto> createReport(@RequestParam Long imageId,
                                                  @RequestParam Long id,
                                                  @RequestParam String analysisResult,
                                                  HttpSession session) {
        System.out.println("Received parameters: imageId=" + imageId + ", id=" + id + ", analysisResult=" + analysisResult);

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            System.out.println("Session ID: " + session.getId() + ", User not logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.out.println("Creating report for user: " + loggedInUser.getEmail() + ", Session ID: " + session.getId());

        ReportDto createdReport = reportService.saveReport(loggedInUser.getId(), imageId, id, analysisResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long reportId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ReportDto report = reportService.getReportByIdAndUserId(reportId, loggedInUser.getId());
        return report != null ? ResponseEntity.ok(report) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean isDeleted = reportService.deleteReport(reportId, loggedInUser.getId());
        return isDeleted ? ResponseEntity.ok("Report deleted successfully") : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("'%s' should be a valid %s and '%s' isn't",
                ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue());
        System.err.println("Type mismatch error in ReportController: " + message);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}