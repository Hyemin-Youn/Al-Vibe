-- 카테고리(category) 테이블 데이터 삽입
INSERT INTO categories(name, description) VALUES
('외식/음료', '식당 서빙, 주방보조, 카페/바리스타, 패스트푸드 등'),
('유통/판매', '편의점, 마트, 의류/화장품 매장 관리, 판촉 등'),
('서비스', '일반 서비스, PC방, 노래방, 찜질방, 영화관 등'),
('사무/학원', '사무보조, 데이터 입력, 강사/과외/교육 등'),
('생산/건설', '물류/상하차, 소화물 분류, 포장/품질검사, 제조 등'),
('운송/배달', '배달 대행, 퀵서비스 등'),
('기타', '안내/상담, 이벤트/연예, 역할대행, 야미바이토(일본) 등');

-- 회원(members) 샘플 데이터 삽입
-- 비밀번호는 모두 'Test1234!' 를 BCrypt 해싱한 값
INSERT INTO members (email, password, nickname, role, status) VALUES
('admin@alvibe.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '관리자',     'ADMIN', 'ACTIVE'),
('kim@test.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '김알바',     'USER',  'ACTIVE'),
('lee@test.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '이편의점',    'USER',  'ACTIVE'),
('park@test.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '박카페',     'USER',  'ACTIVE'),
('choi@test.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '최물류',     'USER',  'ACTIVE');
-- id: 1 =관리자, 2 =김알바, 3 =이편의점, 4 =박카페, 5 =최물류

-- 질문 게시판(question_board) 샘플 데이터 삽입
INSERT INTO question_board (member_id, category_id, title, content, view_count, is_deleted) VALUES
(2, 1, '카페 알바 처음인데 음료 만드는 거 어렵나요?', '이번에 카페 알바 구했는데 음료 만드는 게 어렵지 않은지 궁금합니다.',  15, FALSE),
(3, 2, '편의점 알바 야간 수당 어떻게 계산하나요?', '야간 수당이 1.5배라고 들었는데 정확한 계산 방법이 궁금해요.',         42, FALSE),
(4, 3, '노래방 알바 손님 없을 때 뭐 하면 되나요?', '손님이 없는 시간에는 어떻게 시간을 보내는지 알고 싶어요.',            8,  FALSE),
(2, 6, '배달 알바 오토바이 없어도 되나요?', '자전거나 도보로 배달 알바 가능한 곳이 있는지 궁금합니다.',            	   5,  FALSE),
-- 관리자가 삭제한 게시글 (욕설/부적절 내용)
(3, 1, '진짜 짜증나는 진상손님 욕설 글', '(부적절한 내용으로 관리자 삭제)', 3,  TRUE),
(5, 7, '불법 알바 구인 광고', '(스팸/광고성 게시글로 관리자 삭제)', 1,  		 TRUE);
-- id: 1 ~ 4 =정상, 5 ~ 6 =삭제됨

-- 답변(answers) 샘플 데이터 삽입
-- parent_id: 최상위 답변은 자기 자신 참조, FK 체크 일시 해제
SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO answers (id, question_id, member_id, parent_id, content, is_selected, is_deleted) VALUES
-- 질문 1번 답변
(1, 1, 3, 1, '처음엔 좀 헷갈리지만 1~2주면 익숙해져요. 메뉴판 외우는 게 제일 중요해요!', TRUE, FALSE),
(2, 1, 4, 1, '저도 처음엔 힘들었는데 선배 알바가 잘 가르쳐줬어요. 걱정 마세요.', FALSE, FALSE),
(3, 1, 5, 2, '맞아요! 메뉴 외우는 것보다 손 빠르게 움직이는 게 더 중요한 것 같아요.', FALSE, FALSE),
-- 질문 2번 답변
(4, 2, 2, 4, '야간수당은 22:00~06:00 기준으로 시급의 1.5배입니다. 사장님과 꼭 확인하세요.', TRUE, FALSE),
(5, 2, 4, 4, '근로계약서에 야간 수당 명시 여부도 꼭 확인하세요!', FALSE, FALSE),
-- 관리자가 삭제한 답변 (욕설 포함)
(6, 2, 5, 4, '(욕설 포함으로 관리자 삭제)', FALSE, TRUE),
(7, 3, 2, 7, '(스팸성 홍보 댓글로 관리자 삭제)', FALSE, TRUE);
SET FOREIGN_KEY_CHECKS = 1;

-- 신고(reports) 샘플 데이터 삽입
INSERT INTO reports (reporter_id, target_question_id, target_answer_id, target_member_id, reason_category, reason, status, processed_by, processed_at) VALUES
-- 게시글 신고 (처리 완료)
(2, 5, NULL, NULL, 'INAPPROPRIATE', '욕설 및 혐오 표현이 포함된 게시글입니다.',  'RESOLVED', 1, NOW()),
(4, 6, NULL, NULL, 'SPAM',          NULL,                               'RESOLVED', 1, NOW()),
-- 답변 신고 (처리 완료)
(3, NULL, 6, NULL, 'INAPPROPRIATE', NULL,                               'RESOLVED', 1, NOW()),
(5, NULL, 7, NULL, 'SPAM',          '홍보성 링크가 포함되어 있습니다.',         'RESOLVED', 1, NOW()),
-- 미처리 신고
(4, 3, NULL, NULL, 'ILLEGAL',       '불법 알바 모집 의심 내용이 있습니다.',       'PENDING',  NULL, NULL),
(2, NULL, NULL, 5, 'OTHER',         '허위 정보를 반복적으로 게시하는 사용자입니다.', 'PENDING', NULL, NULL);

-- 관리자 활동 로그(admin_logs) 샘플 데이터 삽입
-- 관리자(id=1)가 부적절 게시글/답변 삭제 처리한 이력
INSERT INTO admin_logs (admin_id, action, target_question_id, target_answer_id, target_member_id, detail) VALUES
(1, 'DELETE_POST', 5, NULL, NULL, '욕설 및 혐오 표현 포함 게시글 삭제'),
(1, 'DELETE_POST', 6, NULL, NULL, '스팸/광고성 게시글 삭제'),
(1, 'DELETE_ANSWER',   NULL, 6, NULL, '욕설 포함 답변 삭제'),
(1, 'DELETE_ANSWER',   NULL, 7, NULL, '스팸성 홍보 댓글 삭제');

-- al_vibe DB 조회
/*
SELECT * FROM members;
SELECT * FROM token;
SELECT * FROM categories;
SELECT * FROM question_board;
SELECT * FROM answers;
SELECT * FROM admin_logs;

-- 관리자 페이지: 삭제된 게시글 로그 목록
SELECT
    al.created_at                AS deleted_at,
    m.nickname                   AS admin_nickname,
    al.action,
    q.title                      AS question_title,
    al.detail                    AS reason
FROM admin_logs al
JOIN members m ON m.id = al.admin_id
LEFT JOIN question_board q ON q.id = al.target_question_id
WHERE al.action IN ('DELETE_POST', 'DELETE_ANSWER')
ORDER BY al.created_at DESC;

-- 관리자 페이지: 날짜별 삭제 건수 요약
SELECT
    DATE(created_at)                                    AS date,
    COUNT(CASE WHEN action = 'DELETE_POST'   THEN 1 END) AS deleted_posts,
    COUNT(CASE WHEN action = 'DELETE_ANSWER' THEN 1 END) AS deleted_answers,
    COUNT(*)                                            AS total_deleted
FROM admin_logs
WHERE action IN ('DELETE_POST', 'DELETE_ANSWER')
GROUP BY DATE(created_at)
ORDER BY date DESC;
*/