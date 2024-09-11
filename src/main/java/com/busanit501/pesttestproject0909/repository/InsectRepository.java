package com.busanit501.pesttestproject0909.repository;

import com.busanit501.pesttestproject0909.entity.Insect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsectRepository extends JpaRepository<Insect, Long> {
}