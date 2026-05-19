package com.alvibe.qna.controller;

import com.alvibe.qna.dto.AnswerFormDto;
import com.alvibe.qna.entity.Answer;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.service.AnswerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final MemberRepository memberRepository;

    private Long getMemberId(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."))
                .getId();
    }

    // 답변 작성
    @PostMapping("/questions/{qid}/answers")
    public String createAnswer(
            @PathVariable Long qid,
            @Valid @ModelAttribute AnswerFormDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) return "redirect:/member/login";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerError",
                    bindingResult.getFieldError("content").getDefaultMessage());
            redirectAttributes.addFlashAttribute("answerFormDto", dto);
            return "redirect:/questions/detail/" + qid;
        }

        answerService.createAnswer(qid, dto, getMemberId(userDetails));
        return "redirect:/questions/detail/" + qid;
    }

    //답변 목록 조회
    @GetMapping("/questions/{qid}/answers")
    public String getAnswers(
            @PathVariable Long qid,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("answers", answerService.getAnswersByQuestionId(qid));
        model.addAttribute("adoptedAnswer", answerService.getAdoptedAnswer(qid));
        model.addAttribute("answerFormDto", new AnswerFormDto());

        if (userDetails != null) {
            Long memberId = getMemberId(userDetails);
            model.addAttribute("sessionMemberId", memberId);
        } else {
            model.addAttribute("sessionMemberId", null);
        }

        return "answer/list :: answerSection";
    }

    //답변 수정 처리
    @PostMapping("/answers/{id}/update")
    public String updateAnswer(
            @PathVariable Long id,
            @Valid @ModelAttribute AnswerFormDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {


        if (userDetails == null) return "redirect:/member/login";

        Long questionId = answerService.getQuestionIdByAnswerId(id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerError",
                    bindingResult.getFieldError("content").getDefaultMessage());
            return "redirect:/questions/detail/" + questionId;
        }

        answerService.updateAnswer(id, dto, getMemberId(userDetails));
        return "redirect:/questions/detail/" + questionId;
    }

    //답변 삭제
    @PostMapping("/answers/{id}/delete")
    public String deleteAnswer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) return "redirect:/member/login";

        Long questionId = answerService.getQuestionIdByAnswerId(id);
        answerService.deleteAnswer(id, getMemberId(userDetails));
        return "redirect:/questions/detail/" + questionId;
    }

    // 채택 처리
    @PostMapping("/answers/{id}/adopt")
    public String adoptAnswer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) return "redirect:/member/login";

        try {
            answerService.adoptAnswer(id, getMemberId(userDetails));
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("answerError", e.getMessage());
        }

        Long questionId = answerService.getQuestionIdByAnswerId(id);
        return "redirect:/questions/detail/" + questionId;
    }

    // 채택 취소
    @PostMapping("/answers/{id}/cancel-adopt")
    public String cancelAdopt(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) return "redirect:/member/login";

        try {
            answerService.cancelAdopt(id, getMemberId(userDetails));
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("answerError", e.getMessage());
        }

        Long questionId = answerService.getQuestionIdByAnswerId(id);
        return "redirect:/questions/detail/" + questionId;
    }

    // 대댓글 작성
    @PostMapping("/answers/{id}/comments")
    public String createComment(
            @PathVariable Long id,
            @ModelAttribute AnswerFormDto dto,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) return "redirect:/member/login";

        if (dto.getContent() == null || dto.getContent().trim().length() < 5) {
            redirectAttributes.addFlashAttribute("answerError", "대댓글은 5자 이상 입력해주세요.");
            Long questionId = answerService.getQuestionIdByAnswerId(id);
            return "redirect:/questions/detail/" + questionId;
        }

        answerService.createComment(id, dto, getMemberId(userDetails));

        Long questionId = answerService.getQuestionIdByAnswerId(id);
        return "redirect:/questions/detail/" + questionId;
    }

    // 대댓글 삭제
    @PostMapping("/comments/{id}/delete")
    public String deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) return "redirect:/member/login";

        Long questionId = answerService.getQuestionIdByAnswerId(id);

        try {
            answerService.deleteComment(id, getMemberId(userDetails));
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("answerError", e.getMessage());
        }

        return "redirect:/questions/detail/" + questionId;
    }
}