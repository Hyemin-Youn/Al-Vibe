package com.alvibe.qna.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_answer_id")
    private Answer targetAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_question_id")
    private Question targetQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    @Column(length = 20)
    private String reasonCategory;

    @Column(length = 500)
    private String reason;

    @Column(length = 20)
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private Member processedBy;

    private LocalDateTime processedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}