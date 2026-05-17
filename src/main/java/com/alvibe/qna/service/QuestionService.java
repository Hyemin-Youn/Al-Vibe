package com.alvibe.qna.service;

import com.alvibe.qna.dto.QuestionFormDto;
import com.alvibe.qna.entity.Category;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.entity.Question;
import com.alvibe.qna.repository.CategoryRepository;
import com.alvibe.qna.repository.MemberRepository;
import com.alvibe.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    // 1. 질문 목록 조회
    public List<Question> getAllQuestion(){
        return questionRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
    }

    // 2. 질문 상세 조회
    public Question getQuestionDetail(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));
        question.setViewCount(question.getViewCount() + 1);
        return question;
    }

    // 3. 새로운 글 작성
    public Long createQuestion(QuestionFormDto dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다"));

        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setMember(member);
        question.setCategory(category);

        Question saved = questionRepository.save(question);
        return saved.getId();
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 4. 글 수정
    // 수정용 데이터 가져오기
    public QuestionFormDto getQuestionForm(Long id, Long memberId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        if (!question.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다");
        }

        QuestionFormDto dto = new QuestionFormDto();
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setCategoryId(question.getCategory().getId());
        return dto;
    }
    // 수정 처리
    public void updateQuestion(Long id, QuestionFormDto dto, Long memberId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        if (!question.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다"));
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setCategory(category);
    }

    // 5. 글 삭제
    public void deleteQuestion(Long id, Long memberId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        if (!question.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다");
        }
        question.setDeleted(true); // 실제로 삭제하지 않고 is_deleted 컬럼만 변경
    }
}
