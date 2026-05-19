package com.alvibe.qna.service;

import com.alvibe.qna.dto.ReportFormDto;
import com.alvibe.qna.entity.Answer;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.entity.Report;
import com.alvibe.qna.repository.AnswerRepository;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public void reportAnswer(Long answerId, ReportFormDto dto, Long reporterId) {
        Member reporter = memberRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        // 본인 답변 신고 방지
        if (answer.getMember().getId().equals(reporterId)) {
            throw new IllegalStateException("본인의 답변은 신고할 수 없습니다.");
        }

        // 중복 신고 방지
        if (reportRepository.existsByReporterIdAndTargetAnswerId(reporterId, answerId)) {
            throw new IllegalStateException("이미 신고한 답변입니다.");
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setTargetAnswer(answer);
        report.setReasonCategory(dto.getReasonCategory());
        report.setReason(dto.getReason());
        report.setStatus("PENDING");

        reportRepository.save(report);
    }
}