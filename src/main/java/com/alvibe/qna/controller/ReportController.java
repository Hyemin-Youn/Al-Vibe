package com.alvibe.qna.controller;

import com.alvibe.qna.dto.ReportFormDto;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.service.AnswerService;
import com.alvibe.qna.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final AnswerService answerService;
    private final MemberRepository memberRepository;

    private Long getMemberId(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."))
                .getId();
    }

    @PostMapping("/answers/{id}/report")
    public String reportAnswer(
            @PathVariable Long id,
            @ModelAttribute ReportFormDto dto,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) return "redirect:/member/login";

        try {
            reportService.reportAnswer(id, dto, getMemberId(userDetails));
            redirectAttributes.addFlashAttribute("reportSuccess", "신고가 접수되었습니다.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("reportError", e.getMessage());
        }

        Long questionId = answerService.getQuestionIdByAnswerId(id);
        return "redirect:/questions/detail/" + questionId;
    }
}