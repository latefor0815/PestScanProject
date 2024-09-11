package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.LoginDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/users")
public class LoginController {

    @Autowired
    private UserService userService;

    // 로그인 페이지를 반환하는 GET 요청 처리
    @GetMapping("/login")
    public String loginPage() {
        System.out.println("로그인 페이지 요청됨");  // 로그 추가
        return "user/login";  // 로그인 페이지로 이동
    }

    // 로그인 요청을 처리하는 POST 요청 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto, HttpSession session, Model model) {
        User user = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            System.out.println("로그인 성공: " + user.getEmail());
            return "redirect:/";
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "user/login";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();  // 세션 무효화
            System.out.println("로그아웃 성공");  // 로그 메시지 추가
        }
        return "redirect:/";  // 로그아웃 후 메인 페이지로 리다이렉트
    }
}