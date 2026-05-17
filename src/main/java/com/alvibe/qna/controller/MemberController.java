package com.alvibe.qna.controller;

import com.alvibe.qna.dto.LoginFormDto;
import com.alvibe.qna.dto.MemberSignupDto;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/member/signup")
    public String signUp(MemberSignupDto memberSignupDto, Model model){
        model.addAttribute("memberSignupDto", memberSignupDto);
        return "member/signup";
    }

    @PostMapping("/member/signup")
    public String createMember(@ModelAttribute @Valid MemberSignupDto memberSignupDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/signup";
        }
        memberService.signup(memberSignupDto);
        return "redirect:/member/login";
    }

    @GetMapping("/member/login")
    public String login(LoginFormDto loginFormDto, @RequestParam(value = "error", required = false) String error, Model model){
        if(error != null){
            model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요.");
        }
        model.addAttribute("loginFormDto", loginFormDto);
        return "member/login";
    }
}
