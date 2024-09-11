package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.InsectDto;
import com.busanit501.pesttestproject0909.service.InsectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/insects")
public class InsectController {

    @Autowired
    private InsectService insectService;

    /**
     * 모든 해충 목록 조회 및 페이지 이동
     */
    @GetMapping
    public String showInsectsPage(Model model) {
        List<InsectDto> insects = insectService.getAllInsects();
        model.addAttribute("insectList", insects);
        return "insect";
    }

    /**
     * 해충 ID로 해충 정보 조회
     */
    @GetMapping("/{id}")
    public String getInsectById(@PathVariable Long id, Model model) {
        InsectDto insect = insectService.getInsectById(id);
        model.addAttribute("insect", insect);
        return "insect";
    }

    /**
     * 새로운 해충 정보 생성
     */
    @PostMapping("/create")
    public String createInsect(@ModelAttribute InsectDto insectDto, Model model) {
        insectService.saveInsect(insectDto);
        return "redirect:/insects";  // 저장 후 해충 목록 페이지로 리다이렉트
    }
}