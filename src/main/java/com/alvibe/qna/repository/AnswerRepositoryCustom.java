package com.alvibe.qna.repository;

import com.alvibe.qna.entity.Answer;
import java.util.List;

public interface AnswerRepositoryCustom {

    List<Answer> findAnswersByQuestionIdWithFilter(Long questionId);
    List<Answer> findCommentsByParentId(Long parentId);
}