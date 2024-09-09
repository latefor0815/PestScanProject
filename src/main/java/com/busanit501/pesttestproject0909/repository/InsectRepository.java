package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.Insect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsectRepository extends JpaRepository<Insect, Long> {

    // 해충 이름으로 해충 정보 조회
    Optional<Insect> findByName(String name);

    // 해충 이름이 존재하는지 확인
    boolean existsByName(String name);
}