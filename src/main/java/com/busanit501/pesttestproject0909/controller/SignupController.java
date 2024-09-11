package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.UserDto;
import com.busanit501.pesttestproject0909.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/signup";  // 회원가입 페이지로 이동
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("userDto") UserDto userDto, Model model) {
        if (userService.isUsernameOrEmailExists(userDto.getUsername(), userDto.getEmail())) {
            model.addAttribute("error", "이미 등록된 사용자명 또는 이메일입니다.");
            return "user/signup";  // 중복된 사용자명 오류 시 다시 회원가입 페이지로
        }
        userService.registerUser(userDto);
        return "redirect:/users/login";  // 회원가입 성공 후 로그인 페이지로 리다이렉트
    }
}