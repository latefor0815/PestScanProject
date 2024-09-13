package com.busanit501.pesttestproject0909.controller;

import com.busanit501.pesttestproject0909.dto.ContactDto;
import com.busanit501.pesttestproject0909.entity.Contact;
import com.busanit501.pesttestproject0909.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    // GET 요청: /contact 경로로 이동할 때 contact 페이지를 보여줍니다.
    @GetMapping("/contact")
    public String showContactPage(Model model) {
        model.addAttribute("contactDto", new ContactDto());  // 빈 DTO 객체를 전달
        return "contact";  // contact.html 반환
    }

    // POST 요청: 문의 내용을 제출할 때 처리
    @PostMapping("/submit_contact")
    public String submitContactForm(@ModelAttribute ContactDto contactDto, Model model) {
        // ContactDto에서 Contact 엔티티로 변환
        Contact contact = new Contact(contactDto.getName(), contactDto.getEmail(), contactDto.getMessage());

        // 엔티티를 데이터베이스에 저장
        contactRepository.save(contact);

        model.addAttribute("successMessage", "문의가 성공적으로 접수되었습니다.");
        return "contact";
    }
}