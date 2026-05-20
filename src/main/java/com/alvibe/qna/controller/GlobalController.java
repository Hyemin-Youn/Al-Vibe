package com.alvibe.qna.controller;

import com.alvibe.qna.entity.Category;
import com.alvibe.qna.repository.CategoryRepository;
import com.alvibe.qna.repository.QuestionRepository;
import com.alvibe.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    private final QuestionService questionService;

    @ModelAttribute("categories")
    public List<Category> categories() {
        return questionService.getAllCategoriesForList();
    }
}
