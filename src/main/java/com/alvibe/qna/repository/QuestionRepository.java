package com.alvibe.qna.repository;

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

    // 미답변 검색
    Page<Question> findByAnswersEmpty(Pageable pageable);

    // 제목 검색
    Page<Question> findByTitleContaining(String keyword, Pageable pageable);

    Page<Question> findByContentContaining(String keyword, Pageable pageable);

    Page<Question> findByTitleContainingOrContentContaining(String keyword, String content, Pageable pageable);

    // 세션 비교 -> 조회수 증가
    @Modifying
    @Query("""
            UPDATE Question q
            SET q.viewCount = q.viewCount + 1
            WHERE q.id = :id
            """)
    void increaseView(@Param("id") Long id);

//    List<Question> findTop5ByOrderByLikeCountDesc();

    // 미답변 검색
    Page<Question> findByTitleContainingAndAnswersEmpty(String keyword, Pageable pageable);
}
