package com.alvibe.qna.controller;

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

}
