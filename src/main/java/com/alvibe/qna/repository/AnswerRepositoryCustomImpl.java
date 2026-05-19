package com.alvibe.qna.repository;

import com.alvibe.qna.entity.Answer;
import com.alvibe.qna.entity.QAnswer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AnswerRepositoryCustomImpl implements AnswerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Answer> findAnswersByQuestionIdWithFilter(Long questionId) {
        QAnswer answer = QAnswer.answer;

        return queryFactory
                .selectFrom(answer)
                .where(
                        answer.question().id.eq(questionId),
                        answer.parent().isNull(),
                        answer.isDeleted.isFalse()
                )
                .orderBy(answer.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Answer> findCommentsByParentId(Long parentId) {
        QAnswer answer = QAnswer.answer;

        return queryFactory
                .selectFrom(answer)
                .where(
                        answer.parent().id.eq(parentId),
                        answer.isDeleted.isFalse()
                )
                .orderBy(answer.createdAt.asc())
                .fetch();
    }
}