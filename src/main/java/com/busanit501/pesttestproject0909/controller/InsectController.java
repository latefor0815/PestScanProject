package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.InsectDto;
import com.busanit501.pesttestproject0909.service.InsectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insects")
public class InsectController {

    @Autowired
    private InsectService insectService;

    /**
     * 모든 해충 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<InsectDto>> getAllInsects() {
        List<InsectDto> insects = insectService.getAllInsects();
        return ResponseEntity.ok(insects);
    }

    /**
     * 해충 ID로 해충 정보 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsectDto> getInsectById(@PathVariable Long id) {
        InsectDto insect = insectService.getInsectById(id);
        return ResponseEntity.ok(insect);
    }

    /**
     * 새로운 해충 정보 생성
     */
    @PostMapping("/create")
    public ResponseEntity<InsectDto> createInsect(@RequestBody InsectDto insectDto) {
        InsectDto createdInsect = insectService.saveInsect(insectDto);
        return ResponseEntity.status(201).body(createdInsect);
    }
}