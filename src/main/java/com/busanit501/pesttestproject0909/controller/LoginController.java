package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.LoginDto;
import com.busanit501.pesttestproject0909.service.UserService;
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
    public String login(@ModelAttribute LoginDto loginDto, Model model) {
        boolean isAuthenticated = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());

        if (isAuthenticated) {
            return "redirect:/";  // 로그인 성공 시 index 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "user/login";  // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }

    // index 페이지를 반환하는 GET 요청 처리
    @GetMapping("/")
    public String index() {
        System.out.println("index 페이지 요청됨");
        return "index";
    }
}