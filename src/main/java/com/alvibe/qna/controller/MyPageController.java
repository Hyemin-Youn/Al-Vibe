package com.alvibe.qna.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.alvibe.qna.dto.ChangePasswordRequestDto;
import com.alvibe.qna.dto.MyPageProfileDto;
import com.alvibe.qna.dto.UpdateMyInfoRequestDto;
import com.alvibe.qna.service.MyPageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class MyPageController {
    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService){
        this.myPageService = myPageService;
    }

    @GetMapping("/member/mypage")
    public String mypage(Principal principal, Model model){
       String email = principal.getName();

       MyPageProfileDto myPageProfileDto = myPageService.lookUpMemberByEmail(email);
       model.addAttribute("myPageProfileDto", myPageProfileDto);

       UpdateMyInfoRequestDto updateDto = new UpdateMyInfoRequestDto();
       updateDto.setNickname(myPageProfileDto.getNickname()); // 기존 닉네임
       updateDto.setEmail(myPageProfileDto.getEmail()); // 기존 이메일
        model.addAttribute("updateMyInfoRequestDto", updateDto);

        model.addAttribute("changePasswordRequestDto", new ChangePasswordRequestDto());

       return "member/mypage";
    }

    @PostMapping("/member/mypage/info")
    public String updateMyInfo(Principal principal, @Valid UpdateMyInfoRequestDto updateMyInfoRequestDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            String email = principal.getName();

            MyPageProfileDto myPageProfileDto = myPageService.lookUpMemberByEmail(email);
            model.addAttribute("myPageProfileDto", myPageProfileDto);
            model.addAttribute("changePasswordRequestDto", new ChangePasswordRequestDto());

            return "member/mypage";
        }

        try{
            myPageService.updateMyInfo(principal.getName(), updateMyInfoRequestDto);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                    updateMyInfoRequestDto.getEmail(),
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        } catch (IllegalStateException e) {
            if(e.getMessage().contains("닉네임")){
                bindingResult.rejectValue("nickname", "duplicated", e.getMessage());
            } else if(e.getMessage().contains("이메일")){
                bindingResult.rejectValue("email", "duplicated", e.getMessage());
            } else {
                bindingResult.reject("globalError", e.getMessage());
            }

            String email = principal.getName();

            MyPageProfileDto myPageProfileDto = myPageService.lookUpMemberByEmail(email);
            model.addAttribute("myPageProfileDto", myPageProfileDto);
            model.addAttribute("changePasswordRequestDto", new ChangePasswordRequestDto());

            return "member/mypage";
        }
        return "redirect:/member/mypage";
    }

    @PostMapping("/member/mypage/password")
    public String changePassword(Principal principal, @Valid ChangePasswordRequestDto changePasswordRequestDto, BindingResult bindingResult, Model model, HttpServletRequest request){

        if(!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getNewPasswordConfirm())){
            bindingResult.rejectValue("newPasswordConfirm", "passwordInCorrect", "새 비밀번호가 일치하지 않습니다.");
        }
        if(bindingResult.hasErrors()){
            String email = principal.getName();
            MyPageProfileDto myPageProfileDto = myPageService.lookUpMemberByEmail(email);

            model.addAttribute("myPageProfileDto", myPageProfileDto);

            UpdateMyInfoRequestDto updateDto = new UpdateMyInfoRequestDto();
            updateDto.setNickname(myPageProfileDto.getNickname());
            updateDto.setEmail(myPageProfileDto.getEmail());
            model.addAttribute("updateMyInfoRequestDto", updateDto);

            return "member/mypage";
        }

        try {
            myPageService.changePassword(principal.getName(), changePasswordRequestDto);

            HttpSession session = request.getSession(false);
            if(session != null){
                session.invalidate();
            }
            SecurityContextHolder.clearContext();

        } catch (IllegalStateException e) {
            bindingResult.rejectValue("password", "passwordMismatch", e.getMessage());

            String email = principal.getName();
            MyPageProfileDto myPageProfileDto = myPageService.lookUpMemberByEmail(email);
            model.addAttribute("myPageProfileDto", myPageProfileDto);

            UpdateMyInfoRequestDto updateDto = new UpdateMyInfoRequestDto();
            updateDto.setNickname(myPageProfileDto.getNickname());
            updateDto.setEmail(myPageProfileDto.getEmail());
            model.addAttribute("updateMyInfoRequestDto", updateDto);

            return "member/mypage";
        }

        return "redirect:/questions/list";
    }
}
