package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.LoginDto;
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

    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());  // 실제로는 암호화 필요
        userRepository.save(user);
    }

    public String loginUser(LoginDto loginDto) {
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if (user.isPresent() && user.get().getPassword().equals(loginDto.getPassword())) {
            // JWT 토큰 생성 (간단한 예)
            return "JWT Token";
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
