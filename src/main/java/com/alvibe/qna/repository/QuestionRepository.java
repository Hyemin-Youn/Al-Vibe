package com.alvibe.qna.repository;

import com.alvibe.qna.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByIsDeletedFalseOrderByCreatedAtDesc();

    // 미답변 검색
//    Page<Question> findByAnswersEmpty(Pageable pageable);

    // 제목 검색
    Page<Question> findByTitleContaining(String keyword, Pageable pageable);

    // 미답변 검색
//    Page<Question> findByTitleContainingAndAnswersEmpty(String keyword, Pageable pageable);
}
