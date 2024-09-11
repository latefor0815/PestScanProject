package com.busanit501.pesttestproject0909.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션이 존재하면 무효화
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        // 로그아웃 후 메인 페이지로 리디렉트
        return "redirect:/"; // 메인 페이지로 리디렉트
    }
}