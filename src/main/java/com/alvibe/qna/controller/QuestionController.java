package com.alvibe.qna.controller;

import com.alvibe.qna.dto.QuestionFormDto;
import com.alvibe.qna.entity.Question;
import com.alvibe.qna.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String list(Model model,
                       @RequestParam(value="page", defaultValue = "0") int page,
                       @RequestParam(value="keyword", defaultValue = "") String keyword,
                       @RequestParam(value="sort", defaultValue = "latest") String sort) {

        Page<Question> questionPage = questionService.getQuestionPage(page, keyword, sort);

        model.addAttribute("questionPage", questionPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);

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

    // 질문 목록 조회 (페이지 번호, 사이즈, 정렬 기준)
    @GetMapping
    public String getQuestions(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

//        Page<Question> questionPage = questionService.getAllQuestion();
//
//        model.addAttribute("questionPage", questionPage);
        return "/";
    }

//    // 질문 조회수 증가
//    @PostMapping("/{pid}/view")
//    public String view(@PathVariable("pid") int pid) {
//
//    }
//
//    // 인기 Q&A
//    @GetMapping("/{pid}/popular")
//    public String popular(@PathVariable("pid") int pid) {
//
//    }
//
//    // 연관 Q&A
//    @GetMapping("/{pid}/related")
//    public String related(@PathVariable("pid") int pid) {
//
//    }

}
