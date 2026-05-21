package com.alvibe.qna.repository;

import com.alvibe.qna.entity.Answer;
import com.alvibe.qna.entity.Category;
import com.alvibe.qna.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByIsDeletedFalseOrderByCreatedAtDesc();

    // 세션 비교 -> 조회수 증가
    @Modifying
    @Query("""
            UPDATE Question q
            SET q.viewCount = q.viewCount + 1
            WHERE q.id = :id
            """)
    void increaseView(@Param("id") Long id);

    // 미답변 검색
    Page<Question> findByAnswersEmpty(Pageable pageable);

    // 제목 검색
    Page<Question> findByTitleContaining(String keyword, Pageable pageable);

    Page<Question> findByContentContaining(String keyword, Pageable pageable);

    Page<Question> findByTitleContainingOrContentContaining(String keyword, String content, Pageable pageable);

    Page<Question> findByTitleContainingAndAnswersEmpty(String keyword, Pageable pageable);

    Page<Question> findByTitleContainingOrContentContainingAndAnswersEmpty(String title, String content, Pageable pageable);

    Page<Question> findByContentContainingAndAnswersEmpty(String keyword, Pageable pageable);

    Page<Question> findByCategory(Category category, Pageable pageable);

    Page<Question> findByCategoryAndContentContaining(Category category, String content, Pageable pageable);

    Page<Question> findByCategoryAndTitleContainingOrCategoryAndContentContaining(Category category, String title, Category category2, String content, Pageable pageable);

    Page<Question> findByCategoryAndTitleContaining(Category category, String title, Pageable pageable);

    // 회원이 작성한 질문 개수
    long countByMember_IdAndIsDeletedFalse(Long memberId);

    // 회원이 작성한 질문 목록
    List<Question> findByMember_IdAndIsDeletedFalseOrderByCreatedAtDesc(Long memberId);

    long countByCategory(Category category);

    Page<Question> findByCategoryAndAnswersEmpty(Category category, Pageable pageable);

    Page<Question> findByCategoryAndContentContainingAndAnswersEmpty(Category category, String keyword, Pageable pageable);

    Page<Question> findByCategoryAndTitleContainingOrCategoryAndContentContainingAndAnswersEmpty(Category category, String keyword, Category category1, String keyword1, Pageable pageable);

    Page<Question> findByCategoryAndTitleContainingAndAnswersEmpty(Category category, String keyword, Pageable pageable);
}
