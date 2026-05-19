-- ==========================================
-- al_vibe 샘플 데이터
-- ==========================================

USE al_vibe;

-- =============================================
-- 카테고리(categories) 샘플 데이터
-- =============================================
INSERT INTO categories (name, description) VALUES
                                               ('외식/음료', '식당 서빙, 주방보조, 카페/바리스타, 패스트푸드 등'),
                                               ('유통/판매', '편의점, 마트, 의류/화장품 매장 관리, 판촉 등'),
                                               ('서비스',   '일반 서비스, PC방, 노래방, 찜질방, 영화관 등'),
                                               ('사무/학원', '사무보조, 데이터 입력, 강사/과외/교육 등'),
                                               ('생산/건설', '물류/상하차, 소화물 분류, 포장/품질검사, 제조 등'),
                                               ('운송/배달', '배달 대행, 퀵서비스 등'),
                                               ('기타',     '안내/상담, 이벤트/연예, 역할대행, 야미바이토(일본) 등');
-- category_id: 1=외식/음료, 2=유통/판매, 3=서비스, 4=사무/학원, 5=생산/건설, 6=운송/배달, 7=기타

-- =============================================
-- 회원(members) 샘플 데이터
-- 비밀번호: 'Test1234!' BCrypt 해싱값
-- =============================================
INSERT INTO members (email, password, nickname, role, status, sanctions_count) VALUES
                                                                                   ('admin@alvibe.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '관리자',   'ADMIN', 'ACTIVE', 0),
                                                                                   ('kim@test.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '김알바',   'USER',  'ACTIVE', 0),
                                                                                   ('lee@test.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '이편의점', 'USER',  'ACTIVE', 0),
                                                                                   ('park@test.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '박카페',   'USER',  'ACTIVE', 0),
                                                                                   ('choi@test.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '최물류',   'USER',  'LOCK',   2);
-- id: 1=관리자, 2=김알바, 3=이편의점, 4=박카페, 5=최물류(제재 2회로 LOCK 상태)

-- =============================================
-- 토큰(token) 샘플 데이터
-- =============================================
INSERT INTO token (member_id, token_value, expiry_date) VALUES
                                                            (2, 'eyJhbGciOiJIUzI1NiJ9.kim_refresh_token',  DATE_ADD(NOW(), INTERVAL 7 DAY)),
                                                            (3, 'eyJhbGciOiJIUzI1NiJ9.lee_refresh_token',  DATE_ADD(NOW(), INTERVAL 7 DAY)),
                                                            (4, 'eyJhbGciOiJIUzI1NiJ9.park_refresh_token', DATE_ADD(NOW(), INTERVAL 7 DAY));
-- 최물류(id=5)는 LOCK 상태라 토큰 없음

-- =============================================
-- 질문 게시판(question_board) 샘플 데이터
-- =============================================
INSERT INTO question_board (member_id, category_id, title, content, view_count, is_deleted) VALUES
                                                                                                (2, 1, '카페 알바 처음인데 음료 만드는 거 어렵나요?',   '이번에 카페 알바 구했는데 음료 만드는 게 어렵지 않은지 궁금합니다.', 15, FALSE),
                                                                                                (3, 2, '편의점 알바 야간 수당 어떻게 계산하나요?',      '야간 수당이 1.5배라고 들었는데 정확한 계산 방법이 궁금해요.',        42, FALSE),
                                                                                                (4, 3, '노래방 알바 손님 없을 때 뭐 하면 되나요?',      '손님이 없는 시간에는 어떻게 시간을 보내는지 알고 싶어요.',            8, FALSE),
                                                                                                (2, 6, '배달 알바 오토바이 없어도 되나요?',             '자전거나 도보로 배달 알바 가능한 곳이 있는지 궁금합니다.',            5, FALSE),
                                                                                                (3, 1, '진짜 짜증나는 진상손님 욕설 글',               '(부적절한 내용으로 관리자 삭제)',                                    3, TRUE),   -- 관리자 삭제
                                                                                                (5, 7, '불법 알바 구인 광고',                          '(스팸/광고성 게시글로 관리자 삭제)',                                  1, TRUE);   -- 관리자 삭제
-- id: 1~4=정상, 5~6=삭제됨

-- =============================================
-- 답변(answers) 샘플 데이터
-- ✅ parent_id NULL: 최상위 댓글
-- ✅ parent_id = 부모ID: 대댓글
-- ✅ SET FOREIGN_KEY_CHECKS 꼼수 제거
-- =============================================
INSERT INTO answers (id, question_id, member_id, parent_id, content, is_selected, is_deleted) VALUES
-- 질문 1번 답변
(1, 1, 3, NULL, '처음엔 좀 헷갈리지만 1~2주면 익숙해져요. 메뉴판 외우는 게 제일 중요해요!', TRUE,  FALSE),  -- 최상위, 채택됨
(2, 1, 4, NULL, '저도 처음엔 힘들었는데 선배 알바가 잘 가르쳐줬어요. 걱정 마세요.',         FALSE, FALSE),  -- 최상위
(3, 1, 5, 2,    '맞아요! 메뉴 외우는 것보다 손 빠르게 움직이는 게 더 중요한 것 같아요.',    FALSE, FALSE),  -- 2번 대댓글

-- 질문 2번 답변
(4, 2, 2, NULL, '야간수당은 22:00~06:00 기준으로 시급의 1.5배입니다. 사장님과 꼭 확인하세요.', TRUE,  FALSE),  -- 최상위, 채택됨
(5, 2, 4, NULL, '근로계약서에 야간 수당 명시 여부도 꼭 확인하세요!',                          FALSE, FALSE),  -- 최상위

-- 관리자가 삭제한 답변
(6, 2, 5, NULL, '(욕설 포함으로 관리자 삭제)',      FALSE, TRUE),   -- 최상위, 삭제됨
(7, 3, 2, NULL, '(스팸성 홍보 댓글로 관리자 삭제)', FALSE, TRUE);   -- 최상위, 삭제됨

-- =============================================
-- 공지사항(notices) 샘플 데이터
-- =============================================
INSERT INTO notices (admin_id, title, content, is_pinned, view_count, is_deleted) VALUES
                                                                                      (1, '[필독] 알바이브 서비스 이용 규칙',       '욕설, 허위정보, 스팸 게시글은 즉시 삭제 및 제재 처리됩니다.',  TRUE,  120, FALSE),
                                                                                      (1, '2025년 최저시급 안내',                  '2025년 최저시급은 10,030원으로 확정되었습니다.',               FALSE, 85,  FALSE),
                                                                                      (1, '[점검] 2025.06.01 서버 점검 안내',      '06월 01일 02:00~04:00 서버 점검이 예정되어 있습니다.',         FALSE, 43,  FALSE);

-- =============================================
-- 신고(reports) 샘플 데이터
-- =============================================
INSERT INTO reports (reporter_id, target_question_id, target_answer_id, target_member_id, reason_category, reason, status, processed_by, processed_at) VALUES
-- 처리 완료된 신고
(2, 5,    NULL, NULL, 'INAPPROPRIATE', '욕설 및 혐오 표현이 포함된 게시글입니다.',       'RESOLVED', 1, NOW()),
(4, 6,    NULL, NULL, 'SPAM',          NULL,                                             'RESOLVED', 1, NOW()),
(3, NULL, 6,    NULL, 'INAPPROPRIATE', NULL,                                             'RESOLVED', 1, NOW()),
(5, NULL, 7,    NULL, 'SPAM',          '홍보성 링크가 포함되어 있습니다.',                'RESOLVED', 1, NOW()),
-- 미처리 신고
(4, 3,    NULL, NULL, 'ILLEGAL',       '불법 알바 모집 의심 내용이 있습니다.',            'PENDING',  NULL, NULL),
(2, NULL, NULL, 5,    'OTHER',         '허위 정보를 반복적으로 게시하는 사용자입니다.',   'PENDING',  NULL, NULL);
-- report id: 1~4=처리완료, 5~6=미처리

-- =============================================
-- 회원 제재(member_sanctions) 샘플 데이터
-- 최물류(id=5): 제재 2회 → sanctions_count=2, status=LOCK
-- =============================================
INSERT INTO member_sanctions (member_id, admin_id, sanction_type, reason, start_at, end_at, is_active, related_report_id) VALUES
                                                                                                                              (5, 1, 'WARN', '스팸성 홍보 댓글 반복 작성',          NOW(), DATE_ADD(NOW(), INTERVAL 7  DAY),  FALSE, 4),  -- 1차 경고(만료)
                                                                                                                              (5, 1, 'LOCK', '욕설 및 불법 정보 게시글 반복 위반',  NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY),  TRUE,  3);  -- 2차 잠금(활성)

-- =============================================
-- 관리자 활동 로그(admin_logs) 샘플 데이터
-- =============================================
INSERT INTO admin_logs (admin_id, action, target_question_id, target_answer_id, target_member_id, detail) VALUES
                                                                                                              (1, 'DELETE_POST',   5,    NULL, NULL, '욕설 및 혐오 표현 포함 게시글 삭제'),
                                                                                                              (1, 'DELETE_POST',   6,    NULL, NULL, '스팸/광고성 게시글 삭제'),
                                                                                                              (1, 'DELETE_ANSWER', NULL, 6,    NULL, '욕설 포함 답변 삭제'),
                                                                                                              (1, 'DELETE_ANSWER', NULL, 7,    NULL, '스팸성 홍보 댓글 삭제'),
                                                                                                              (1, 'WARN_MEMBER',   NULL, NULL, 5,    '1차 경고: 스팸성 홍보 댓글 반복 작성'),
                                                                                                              (1, 'LOCK_MEMBER',   NULL, NULL, 5,    '2차 제재: 욕설 및 불법 정보 반복 위반 → 30일 잠금');

-- =============================================
-- 데이터 확인 쿼리
-- =============================================
/*
SELECT * FROM members;
SELECT * FROM token;
SELECT * FROM categories;
SELECT * FROM question_board;
SELECT * FROM answers;
SELECT * FROM notices;
SELECT * FROM reports;
SELECT * FROM member_sanctions;
SELECT * FROM admin_logs;

-- 미처리 신고 목록
SELECT
    r.id            AS report_id,
    m.nickname      AS reporter,
    r.reason_category,
    r.reason,
    r.created_at
FROM reports r
JOIN members m ON m.id = r.reporter_id
WHERE r.status = 'PENDING'
ORDER BY r.created_at DESC;

-- 제재 이력 + 회원 정보
SELECT
    ms.id            AS sanction_id,
    m.nickname       AS member,
    m.sanctions_count,
    ms.sanction_type,
    ms.reason,
    ms.start_at,
    ms.end_at,
    ms.is_active
FROM member_sanctions ms
JOIN members m ON m.id = ms.member_id
ORDER BY ms.created_at DESC;

-- 관리자 활동 로그 목록
SELECT
    al.created_at   AS action_at,
    m.nickname      AS admin,
    al.action,
    al.detail
FROM admin_logs al
JOIN members m ON m.id = al.admin_id
ORDER BY al.created_at DESC;
*/
