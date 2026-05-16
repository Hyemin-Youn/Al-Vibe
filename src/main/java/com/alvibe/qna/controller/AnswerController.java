package com.alvibe.qna.controller;

import com.alvibe.qna.dto.AnswerFormDto;
import com.alvibe.qna.entity.Answer;
import com.alvibe.qna.service.AnswerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // ─────────────────────────────────────────────────────────
    // POST /questions/{qid}/answers  → 답변 작성
    // ─────────────────────────────────────────────────────────
    @PostMapping("/questions/{qid}/answers")
    public String createAnswer(
            @PathVariable Long qid,
            @Valid @ModelAttribute AnswerFormDto dto,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 로그인 여부 확인
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/member/login";
        }

        // 유효성 검사 실패 시 질문 상세 페이지로 돌아가기
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerError",
                    bindingResult.getFieldError("content").getDefaultMessage());
            redirectAttributes.addFlashAttribute("answerFormDto", dto);
            return "redirect:/questions/" + qid;
        }

        answerService.createAnswer(qid, dto, memberId);
        return "redirect:/questions/" + qid;
    }

    // ─────────────────────────────────────────────────────────
    // GET /questions/{qid}/answers  → 답변 목록 조회
    //  (질문 상세 페이지에서 Fragment 또는 AJAX 용도로 활용 가능)
    // ─────────────────────────────────────────────────────────
    @GetMapping("/questions/{qid}/answers")
    public String getAnswers(
            @PathVariable Long qid,
            Model model) {

        List<Answer> answers = answerService.getAnswersByQuestionId(qid);
        model.addAttribute("answers", answers);
        model.addAttribute("questionId", qid);
        return "answer/list"; // templates/answer/list.html
    }

    // ─────────────────────────────────────────────────────────
    // POST /answers/{id}/update  → 답변 수정 처리
    // ─────────────────────────────────────────────────────────
    @PostMapping("/answers/{id}/update")
    public String updateAnswer(
            @PathVariable Long id,
            @Valid @ModelAttribute AnswerFormDto dto,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/member/login";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerError",
                    bindingResult.getFieldError("content").getDefaultMessage());
            Long questionId = answerService.getQuestionIdByAnswerId(id);
            return "redirect:/questions/" + questionId;
        }

        Long questionId = answerService.getQuestionIdByAnswerId(id);
        answerService.updateAnswer(id, dto, memberId);
        return "redirect:/questions/" + questionId;
    }

    //답변 삭제
    @PostMapping("/answers/{id}/delete")
    public String deleteAnswer(
            @PathVariable Long id,
            HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/member/login";
        }

        // 삭제 전에 questionId 먼저 조회 (삭제 후엔 조회 불가)
        Long questionId = answerService.getQuestionIdByAnswerId(id);

        answerService.deleteAnswer(id, memberId);
        return "redirect:/questions/" + questionId;
    }
}