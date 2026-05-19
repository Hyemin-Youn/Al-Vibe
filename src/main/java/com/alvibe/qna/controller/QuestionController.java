package com.alvibe.qna.controller;

import com.alvibe.qna.dto.AnswerFormDto;
import com.alvibe.qna.dto.QuestionFormDto;
import com.alvibe.qna.entity.Category;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.entity.Question;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.service.AnswerService;
import com.alvibe.qna.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final MemberRepository memberRepository;
    private final AnswerService answerService;

    public QuestionController(QuestionService questionService,
                              MemberRepository memberRepository,
                              AnswerService answerService) {
        this.questionService = questionService;
        this.memberRepository = memberRepository;
        this.answerService = answerService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer categoryId, Model model,
                       @RequestParam(value="page", defaultValue = "0") int page,
                       @RequestParam(value="keyword", defaultValue = "") String keyword,
                       @RequestParam(value="sort", defaultValue = "latest") String sort,
                       @RequestParam(value="searchType", defaultValue = "title") String searchType,
                       @RequestParam(value="category", required=false) String categoryName) {

        Page<Question> questionPage = questionService.getQuestionPage(page, keyword, sort, searchType, categoryName);

        model.addAttribute("questionPage", questionPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("searchType", searchType);
        model.addAttribute("category", categoryName);

        // 임시 카테고리 아이콘 생성
        model.addAttribute("categories", questionService.getAllCategories());
        model.addAttribute("currentCategoryId", categoryId);

        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
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
        model.addAttribute("answers", answerService.getAnswersByQuestionId(id));
        model.addAttribute("adoptedAnswer", answerService.getAdoptedAnswer(id));

        if (userDetails != null) {
            Long memberId = memberRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow().getId();
            model.addAttribute("sessionMemberId", memberId);
        } else {
            model.addAttribute("sessionMemberId", null);
        }

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
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", questionService.getAllCategories());
            return "question/form";
        }

        Long memberId = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("로그인 정보 오류"))
                .getId();

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

        QuestionFormDto dto = questionService.getQuestionForm(id, memberId);

        model.addAttribute("questionFormDto", dto);
        model.addAttribute("categories", questionService.getAllCategories());
        model.addAttribute("questionId", id);
        return "question/edit";
    }

    // 수정 처리
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute QuestionFormDto questionFormDto,
                       @AuthenticationPrincipal UserDetails userDetails,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", questionService.getAllCategories());
            model.addAttribute("questionId", id);
            return "question/edit";
        }

        Long memberId = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("로그인 정보 오류"))
                .getId();

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

//
//    // 연관 Q&A
//    @GetMapping("/{pid}/related")
//    public String related(@PathVariable("pid") int pid) {
//
//    }

}
