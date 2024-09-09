package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 검색
    Optional<User> findByEmail(String email);

    // 사용자명이 존재하는지 확인
    boolean existsByUsername(String username);

    // 이메일이 존재하는지 확인
    boolean existsByEmail(String email);
}