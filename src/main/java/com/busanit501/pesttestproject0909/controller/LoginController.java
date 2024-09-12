package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.LoginDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto, HttpSession session, Model model) {
        User user = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            System.out.println("로그인 성공: " + user.getEmail() + ", Session ID: " + session.getId());
            System.out.println("세션 속성: " + session.getAttribute("loggedInUser"));
            return "redirect:/";
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        System.out.println("로그아웃 전 Session ID: " + session.getId());
        session.invalidate();
        System.out.println("로그아웃 후 새 Session ID: " + request.getSession(true).getId());
        return "redirect:/";
    }

    @GetMapping("/status")
    @ResponseBody
    public String loginStatus(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return "로그인 상태: " + loggedInUser.getEmail() + ", Session ID: " + session.getId();
        } else {
            return "로그인되지 않음, Session ID: " + session.getId();
        }
    }
}