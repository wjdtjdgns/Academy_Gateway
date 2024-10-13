package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.MemberDto;
import com.nhnacademy.miniDooray.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/register")
    public String registerMember(@ModelAttribute MemberDto memberDto, Model model) {
        try {
            memberService.registerMember(memberDto);
            return "registrationSuccess";
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                model.addAttribute("error", "아이디가 이미 존재합니다.");
                return "registerForm";
            } else {
                model.addAttribute("error", "유효하지않은 데이터입니다.");
                return "registerForm";
            }
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "registerForm";
    }
}
