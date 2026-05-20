package com.alvibe.qna.repository;

import com.alvibe.qna.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom {
    // 회원이 작성한 답변 목록
    List<Answer> findByMemberIdAndIsDeletedFalseAndParentIsNull(Long memberId);

    // 회원이 작성한 답변 수
    int countByMemberIdAndIsDeletedFalseAndParentIsNull(Long memberId);

    // 회원이 채택된 답변 수
    int countByMemberIdAndIsSelectedTrueAndIsDeletedFalse(Long memberId);
}