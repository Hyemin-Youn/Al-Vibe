package com.alvibe.qna.controller;


import com.alvibe.qna.dto.AnswerFormDto;
import com.alvibe.qna.dto.QuestionFormDto;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.entity.Question;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.service.AnswerService;
import com.alvibe.qna.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberRepository memberRepository;
    private final AnswerService answerService; //답변 기능 추가

    @GetMapping("/list")
    public String list(Model model) {
        List<Question> questions = questionService.getAllQuestion();
        model.addAttribute("questions", questions);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Question question = questionService.getQuestionDetail(id);
        model.addAttribute("question", question);

        boolean isAuthor = false;
        if (userDetails != null) {
            isAuthor = userDetails.getUsername().equals(question.getMember().getEmail());
        }
        model.addAttribute("isAuthor", isAuthor);

        // detail.html에서 답변 영역 추가
        model.addAttribute("questionId", id);
        model.addAttribute("answerFormDto", new AnswerFormDto());
        // 병합 후 아래 3줄 삭제 필요
        model.addAttribute("answers", answerService.getAnswersByQuestionId(id));
        model.addAttribute("adoptedAnswer", answerService.getAdoptedAnswer(id));
        model.addAttribute("sessionMemberId", 2L);  // 임시 하드코딩


        return "question/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("questionFormDto", new QuestionFormDto());
        model.addAttribute("categories", questionService.getAllCategories());
        return "question/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute QuestionFormDto questionFormDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", questionService.getAllCategories());
            return "question/form";
        }

        Long memberId = 1L;  // 임시 테스트 유저

        Long savedId = questionService.createQuestion(questionFormDto, memberId);
        return "redirect:/questions/detail/" + savedId;
    }

    // 수정 폼 가져오기
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {

        if (userDetails == null) {
            return "redirect:/member/login";
        }

        Long memberId = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("로그인 정보 오류"))
                .getId();

        QuestionFormDto dto = questionService.getQuestionForm(id);

        model.addAttribute("questionFormDto", dto);
        model.addAttribute("categories", questionService.getAllCategories());
        model.addAttribute("questionId", id);
        return "question/edit";
    }

    // 수정 처리
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute QuestionFormDto questionFormDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", questionService.getAllCategories());
            model.addAttribute("questionId", id);
            return "question/edit";
        }

        Long memberId = 1L;   // 임시로 id 1번 유저 강제

        questionService.updateQuestion(id, questionFormDto, memberId);
        return "redirect:/questions/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        Long memberId = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("로그인 정보 오류"))
                .getId();

        questionService.deleteQuestion(id, memberId);
        return "redirect:/questions/list";
    }
}