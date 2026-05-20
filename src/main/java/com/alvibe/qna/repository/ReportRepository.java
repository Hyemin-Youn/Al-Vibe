package com.alvibe.qna.repository;

import com.alvibe.qna.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // 중복 신고 방지
    boolean existsByReporterIdAndTargetAnswerId(Long reporterId, Long answerId);

    // 질문 중복 신고 방지
    boolean existsByReporterIdAndTargetQuestionId(Long reporterId, Long questionId);
}