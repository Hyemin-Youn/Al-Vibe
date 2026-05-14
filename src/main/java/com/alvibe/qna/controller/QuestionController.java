package com.alvibe.qna.controller;

import com.alvibe.qna.dto.QuestionFormDto;
import com.alvibe.qna.entity.Question;
import com.alvibe.qna.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/list")
    public String list(Model model) {
        List<Question> questions = questionService.getAllQuestion();
        model.addAttribute("questions", questions);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Question question = questionService.getQuestionDetail(id);
        model.addAttribute("question", question);
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
    public String editForm(@PathVariable Long id, Model model) {
        QuestionFormDto dto = questionService.getQuestionForm(id);
        model.addAttribute("questionFormDto", dto);
        model.addAttribute("categories", questionService.getAllCategories());
        model.addAttribute("questionId", id);   // 폼 action 에서 사용
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
    public String delete(@PathVariable Long id) {
        Long memberId = 1L;   // TODO: Security 적용 후 실제 로그인 유저로

        questionService.deleteQuestion(id, memberId);
        return "redirect:/questions/list";
    }
}
