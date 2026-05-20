package com.alvibe.qna.service;

import com.alvibe.qna.dto.AnswerFormDto;
import com.alvibe.qna.entity.Answer;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.entity.Question;
import com.alvibe.qna.repository.AnswerRepository;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;


    //특정 질문에 대한 답변 목록 조회
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findAnswersByQuestionIdWithFilter(questionId);
    }

    //답변 작성
    public Long createAnswer(Long questionId, AnswerFormDto dto, Long memberId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다. id=" + questionId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + memberId));

        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setMember(member);
        answer.setContent(dto.getContent());

        Answer saved = answerRepository.save(answer);
        return saved.getId();
    }

    //답변 수정용 데이터 가져오기
    public AnswerFormDto getAnswerForm(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + answerId));

        AnswerFormDto dto = new AnswerFormDto();
        dto.setContent(answer.getContent());
        return dto;
    }

    //답변 수정
    public void updateAnswer(Long answerId, AnswerFormDto dto, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + answerId));

        if (!answer.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }

        answer.setContent(dto.getContent());
    }

    //답변 삭제
    public void deleteAnswer(Long answerId, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + answerId));

        if (!answer.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        answer.setDeleted(true);
    }

    //답변이 속한 questionId 조회
    public Long getQuestionIdByAnswerId(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + answerId));
        return answer.getQuestion().getId();
    }

    //채택 처리
    public void adoptAnswer(Long answerId, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + answerId));

        if (!answer.getQuestion().getMember().getId().equals(memberId)) {
            throw new IllegalStateException("질문 작성자만 채택할 수 있습니다.");
        }

        // 기존 채택된 답변 자동 취소 후 새로 채택
        List<Answer> answers = answerRepository.findAnswersByQuestionIdWithFilter(answer.getQuestion().getId());
        answers.stream()
                .filter(a -> a.isSelected())
                .findFirst()
                .ifPresent(a -> a.setSelected(false));

        answer.setSelected(true);
    }

    //채택된 답변 1개 조회
    public Answer getAdoptedAnswer(Long questionId) {
        return answerRepository.findAnswersByQuestionIdWithFilter(questionId)
                .stream()
                .filter(a -> a.isSelected())
                .findFirst()
                .orElse(null);
    }

    //채택 취소
    public void cancelAdopt(Long answerId, Long memberId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + answerId));

        if (!answer.getQuestion().getMember().getId().equals(memberId)) {
            throw new IllegalStateException("질문 작성자만 채택을 취소할 수 있습니다.");
        }

        answer.setSelected(false);
    }

    //대댓글 작성
    public Long createComment(Long parentAnswerId, AnswerFormDto dto, Long memberId) {
        Answer parentAnswer = answerRepository.findById(parentAnswerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다. id=" + parentAnswerId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + memberId));

        Answer comment = new Answer();
        comment.setQuestion(parentAnswer.getQuestion());
        comment.setMember(member);
        comment.setContent(dto.getContent());
        comment.setParent(parentAnswer);

        return answerRepository.save(comment).getId();
    }

    //대댓글 삭제
    public void deleteComment(Long commentId, Long memberId) {
        Answer comment = answerRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다. id=" + commentId));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        comment.setDeleted(true);
    }

    // 마이페이지에서 사용할 메서드 추가
    public List<Answer> getAnswersByMemberId(Long memberId) {
        return answerRepository.findByMemberIdAndIsDeletedFalseAndParentIsNull(memberId);
    }

    public int countAnswersByMemberId(Long memberId) {
        return answerRepository.countByMemberIdAndIsDeletedFalseAndParentIsNull(memberId);
    }

    public int countSelectedAnswersByMemberId(Long memberId) {
        return answerRepository.countByMemberIdAndIsSelectedTrueAndIsDeletedFalse(memberId);
    }

}