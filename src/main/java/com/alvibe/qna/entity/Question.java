package com.alvibe.qna.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_board")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    private String content;

    private int viewCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDeleted;

    //답변 수 count를 위해 추가
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    // answerCount getter 추가
    public int getAnswerCount() {
        return answers != null ? (int) answers.stream()
                                       .filter(a -> !a.isDeleted())  // 삭제된 답변 제외
                                       .count() : 0;
    }
}
