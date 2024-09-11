package com.busanit501.pesttestproject0909.service;


import com.busanit501.pesttestproject0909.dto.UserDto;
import com.busanit501.pesttestproject0909.entity.User;
import com.busanit501.pesttestproject0909.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 이메일 또는 사용자명이 이미 존재하는지 확인
    public boolean isUsernameOrEmailExists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    // 회원가입 로직
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());  // 실제로는 비밀번호 암호화 필요
        userRepository.save(user);
    }

    // 이메일로 로그인하는 로직
    public boolean loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
}