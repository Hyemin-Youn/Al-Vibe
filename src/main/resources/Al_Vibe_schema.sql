-- DB 생성
CREATE DATABASE IF NOT EXISTS al_vibe;

-- DB 사용
USE al_vibe;

-- [1] 삭제 단계 : 자식 테이블부터 순차적으로 삭제
DROP TABLE IF EXISTS admin_logs;
DROP TABLE IF EXISTS member_sanctions;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS question_board;
DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS members;

-- [2] 생성 단계 : 부모 테이블부터 순차적으로 생성
-- 회원(members) 테이블 생성
CREATE TABLE members (
	id BIGINT AUTO_INCREMENT, 															# 식별자[PK]
    email VARCHAR(100) UNIQUE NOT NULL, 												# 이메일(로그인 ID)
    password VARCHAR(255) NOT NULL,														# 비밀번호
    nickname VARCHAR(20) UNIQUE NOT NULL,												# 닉네임
    role VARCHAR(20) NOT NULL DEFAULT 'USER',											# 권한(ADMIN/USER)
    status VARCHAR(20) DEFAULT 'ACTIVE',												# 계정 상태(ACTIVE/LOCK/LEAVE)
    create_at DATETIME NOT NULL DEFAULT current_timestamp,								# 게정 생성 날짜
    update_at DATETIME NOT NULL	DEFAULT current_timestamp ON UPDATE current_timestamp,	# 계정 정보 수정 날짜
    sanctions_count INT NULL DEFAULT 0,													# 제재 당한 횟수(최대 3회)
    
    PRIMARY KEY (id)
); 
-- 회원가입시 email, password, nickname 작성 필수
-- password는 BCrypt 해싱 처리 필수!

-- role 컬럼을 통해 일반회원/관리자 구분 가능
-- 회원가입시 email을 id로 설정하여 가입 이탈률 감소효과를 줄 수 있음
-- 아이디 자체가 연락처이므로 알림이나 메일 등을 보내기 수월함
-- UNIQUE 제약조건을 통해 email로 회원 조회시 속도가 빨라지는 결과 도출 = 내부적으로 B-Tree 인덱스를 생성하게 되어 회원 조회시
-- Full Scan이 아닌 인덱스를 통해 조회하므로 사용자가 늘어나도 응답 속도가 일정하게 유지됨
-- 로그인 실패 시 5~10분간 잠금 처리를 하는 로직을 추가하면 보안성이 높아짐 (단, DB에도 실패 횟수 컬럼이 필요)

-- 카테고리(categories) 테이블 생성
CREATE TABLE categories (
	category_id INT AUTO_INCREMENT,						# 식별자[PK]
    name VARCHAR(50) UNIQUE NOT NULL,					# 카테고리 이름
    create_at DATETIME DEFAULT current_timestamp,		# 등록일
    description VARCHAR(255),							# 카테고리 설명
    
    PRIMARY KEY (category_id)
);
/*
- 카테고리는 총 7개로 고정
- 카테고리 수정은 ADMIN(관리자) 권한을 갖고 있는 사용자만 접근 가능

1. 외식/음료: 식당 서빙, 주방보조, 카페/바리스타, 패스트푸드
2. 유통/판매: 편의점, 마트, 의류/화장품 매장 관리, 판촉
3. 서비스: 일반 서비스, PC방, 노래방, 찜질방, 영화관
4. 사무/학원: 사무보조, 데이터 입력, 강사/과외/교육
5. 생산/건설: 물류/상하차, 소화물 분류, 포장/품질검사, 제조
6. 운송/배달: 배달 대행, 퀵서비스
7. 기타: 안내/상담, 이벤트/연예, 역할대행, 야미바이토(일본)
*/

-- refresh 토큰 관리(token) 테이블 생성
CREATE TABLE token (
	token_id BIGINT AUTO_INCREMENT,						# 식별자[PK]
    member_id BIGINT UNIQUE NOT NULL,					# 토큰 소유자[FK]
    token_value VARCHAR(255) UNIQUE NOT NULL,			# 실제 Refresh Token
    create_at DATETIME DEFAULT current_timestamp,		# 발급 시점
    expiry_date DATETIME NOT NULL,						# 만료 시점
    
    PRIMARY KEY (token_id),
    CONSTRAINT fk_members_token FOREIGN KEY (member_id) REFERENCES members(id)
);
-- 발급받은 Refresh token 관리 테이블
-- 만료된 Refresh token이 DB에 쌓일 수 있기에 이를 고려하여 주기적으로 삭제하는 로직 추가 필요

-- 질문 게시판(question_board) 테이블 생성
CREATE TABLE question_board (
	id BIGINT AUTO_INCREMENT,																# 게시글 번호[PK]
    member_id BIGINT NOT NULL,																# 질문 작성자 식별자[FK]
    category_id INT NOT NULL,																# 카테고리 식별자[FK]
    title VARCHAR(255) NOT NULL,															# 제목
    content TEXT NOT NULL,																	# 내용
    view_count INT DEFAULT 0,																# 조회수
    created_at DATETIME NOT NULL DEFAULT current_timestamp,									# 작성 시간
    updated_at DATETIME NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,		# 수정 시간
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,											# 삭제표시 여부
    
    PRIMARY KEY (id),
    CONSTRAINT fk_members_question FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT fk_categories_question FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
-- 내용(content) 부분에는 VARCHAR(255) 보다 긴 글이 삽입될 수 있기에 TEXT 사용


-- 답변 (answers) 테이블 생성
CREATE TABLE answers (
	id BIGINT AUTO_INCREMENT,																# 답변 번호[PK]
    question_id BIGINT NOT NULL,															# 관련 질문 번호[FK]
    member_id BIGINT NOT NULL,																# 답변 작성자 식별자[FK]
	parent_id BIGINT NULL,																	# 부모 답변 ID(대댓글용)[FK]
    content TEXT NOT NULL,																	# 답변 내용
    is_selected BOOLEAN DEFAULT FALSE,														# 채택 여부(TRUE/FALSE)
	created_at DATETIME NOT NULL DEFAULT current_timestamp,									# 작성 시간
    updated_at DATETIME NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,		# 수정 시간
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    PRIMARY KEY (id),
    CONSTRAINT fk_question_answers FOREIGN KEY (question_id) REFERENCES question_board(id) ON DELETE CASCADE,
    CONSTRAINT fk_members_answers FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT fk_answers_answers FOREIGN KEY (parent_id) REFERENCES answers(id) ON DELETE CASCADE
);
-- 질문이 삭제되면 관련 답변들도 같이 삭제되도록 CASCADE 설정
-- parent_id는 대댓글용 부모 댓글 지정을 위한 컬럼으로 answers 테이블의 id를 self 참조한다. 단 부모 댓글 삭제시 해당 댓글 같이 삭제됨(CASCADE)
-- 무제한 대댓글 제한이 필요할 수 있음


-- ============================================
-- 관리자 페이지 관련 테이블
-- ============================================

-- 공지사항(notices) 테이블 생성
CREATE TABLE notices (
	id BIGINT AUTO_INCREMENT,																# 식별자[PK]
    admin_id BIGINT NOT NULL,																# 작성 관리자[FK]
    title VARCHAR(255) NOT NULL,															# 제목
    content TEXT NOT NULL,																	# 내용
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,												# 상단 고정 여부
    view_count INT NOT NULL DEFAULT 0,														# 조회수
    created_at DATETIME NOT NULL DEFAULT current_timestamp,									# 작성 시간
    updated_at DATETIME NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,		# 수정 시간
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,												# 삭제 표시

    PRIMARY KEY (id),
    CONSTRAINT fk_members_notices FOREIGN KEY (admin_id) REFERENCES members(id)
);
-- 관리자(role=ADMIN)만 작성/수정/삭제 가능
-- is_pinned=TRUE 공지는 목록 상단에 고정 노출

-- 신고(reports) 테이블 생성
CREATE TABLE reports (
	id BIGINT AUTO_INCREMENT,																# 식별자[PK]
    reporter_id BIGINT NOT NULL,															# 신고자[FK]
    target_question_id BIGINT NULL,															# 신고 대상: 질문[FK]
    target_answer_id BIGINT NULL,															# 신고 대상: 답변[FK]
    target_member_id BIGINT NULL,															# 신고 대상: 회원[FK]
    reason_category ENUM('SPAM', 'INAPPROPRIATE', 'ILLEGAL', 'OTHER') NOT NULL,				# 신고 유형(스팸/욕설·혐오/불법정보/기타)
    reason VARCHAR(500) NULL,																# 상세 사유(기타 선택 시 필수, 나머지 선택)
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',											# 처리상태(PENDING/RESOLVED/REJECTED)
    processed_by BIGINT NULL,																# 처리한 관리자[FK]
    processed_at DATETIME NULL,																# 처리 시점
    created_at DATETIME NOT NULL DEFAULT current_timestamp,									# 신고 시점

    PRIMARY KEY (id),
    CONSTRAINT fk_reporter_reports FOREIGN KEY (reporter_id) REFERENCES members(id),
    CONSTRAINT fk_question_reports FOREIGN KEY (target_question_id) REFERENCES question_board(id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_reports FOREIGN KEY (target_answer_id) REFERENCES answers(id) ON DELETE CASCADE,
    CONSTRAINT fk_target_member_reports FOREIGN KEY (target_member_id) REFERENCES members(id),
    CONSTRAINT fk_processor_reports FOREIGN KEY (processed_by) REFERENCES members(id),
    -- 신고 대상은 정확히 1개만 지정되어야 함
    CONSTRAINT chk_report_target CHECK (
		(CASE WHEN target_question_id IS NOT NULL THEN 1 ELSE 0 END) +
        (CASE WHEN target_answer_id   IS NOT NULL THEN 1 ELSE 0 END) +
        (CASE WHEN target_member_id   IS NOT NULL THEN 1 ELSE 0 END) = 1
	)
);
-- status 흐름: PENDING(접수) → RESOLVED(조치완료) / REJECTED(반려)
-- 중복신고 방지가 필요하면 (reporter_id, target_xxx_id) UNIQUE 인덱스 고려
-- 신고 대상이 삭제되면 신고도 함께 삭제됨(CASCADE)

-- 회원 제재(member_sanctions) 테이블 생성
CREATE TABLE member_sanctions (
	id BIGINT AUTO_INCREMENT,																# 식별자[PK]
    member_id BIGINT NOT NULL,																# 제재 대상 회원[FK]
    admin_id BIGINT NOT NULL,																# 제재 부여 관리자[FK]
    sanction_type VARCHAR(20) NOT NULL,														# 제재 유형(WARN/LOCK/BAN)
    reason VARCHAR(500) NOT NULL,															# 제재 사유
    start_at DATETIME NOT NULL DEFAULT current_timestamp,									# 제재 시작
    end_at DATETIME NULL,																	# 제재 종료(NULL이면 영구)
    is_active BOOLEAN NOT NULL DEFAULT TRUE,												# 제재 활성화 여부(해제 시 FALSE)
    related_report_id BIGINT NULL,															# 관련 신고[FK]
    created_at DATETIME NOT NULL DEFAULT current_timestamp,									# 기록 시점

    PRIMARY KEY (id),
    CONSTRAINT fk_member_sanctions FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT fk_admin_sanctions FOREIGN KEY (admin_id) REFERENCES members(id),
    CONSTRAINT fk_report_sanctions FOREIGN KEY (related_report_id) REFERENCES reports(id) ON DELETE SET NULL
);
-- 제재 적용 시 members.status도 함께 갱신 (WARN=ACTIVE 유지, LOCK/BAN=LOCK)
-- 기간 만료/관리자 해제 시 is_active=FALSE 처리하여 이력 보존
-- 만료된 제재 자동 해제는 스케줄러로 end_at < NOW() 대상 일괄 처리

-- 관리자 활동 로그(admin_logs) 테이블 생성
CREATE TABLE admin_logs (
	id BIGINT AUTO_INCREMENT,																# 식별자[PK]
    admin_id BIGINT NOT NULL,																# 행위자(관리자)[FK]
    action VARCHAR(50) NOT NULL,															# 수행 행동(DELETE_QUESTION, BAN_MEMBER 등)
    target_question_id BIGINT NULL,															# 대상: 질문[FK]
    target_answer_id BIGINT NULL,															# 대상: 답변[FK]
    target_member_id BIGINT NULL,															# 대상: 회원[FK]
    detail VARCHAR(500) NULL,																# 상세 메모/사유
    created_at DATETIME NOT NULL DEFAULT current_timestamp,									# 행위 시점

    PRIMARY KEY (id),
    CONSTRAINT fk_admin_admin_logs FOREIGN KEY (admin_id) REFERENCES members(id),
    CONSTRAINT fk_question_admin_logs FOREIGN KEY (target_question_id) REFERENCES question_board(id) ON DELETE SET NULL,
    CONSTRAINT fk_answer_admin_logs FOREIGN KEY (target_answer_id) REFERENCES answers(id) ON DELETE SET NULL,
    CONSTRAINT fk_target_member_admin_logs FOREIGN KEY (target_member_id) REFERENCES members(id)
);
-- 감사 로그는 절대 삭제하지 않음(soft delete 컬럼 없음)
-- 원본 데이터가 삭제되어도 로그는 남아야 하므로 ON DELETE SET NULL 정책


-- [3] 인덱스생성 단계 : 조회 성능 최적화
-- 카테고리별 최신순 조회용 인덱스 생성
CREATE INDEX idx_question_category_create ON question_board (category_id, created_at DESC);
-- 작성자별 글 목록 조회용 인덱스 생성
CREATE INDEX idx_question_member ON question_board (member_id);
-- 특정 질문의 답변을 최신순으로 불러올 인덱스 생성
CREATE INDEX idx_answer_question_date ON answers (question_id, created_at DESC);
-- 특정 사용자가 작성한 답변을 최신순으로 불러올 인덱스 생성 (마이페이지/활동로그)
CREATE INDEX idx_answer_member_date ON answers (member_id, created_at DESC);

-- 관리자 페이지 인덱스
-- 신고 처리 대시보드: 미처리 신고 최신순
CREATE INDEX idx_reports_status_create ON reports (status, created_at DESC);
-- 특정 회원이 받은 신고 조회
CREATE INDEX idx_reports_target_member ON reports (target_member_id);
-- 공지사항 목록: 고정 우선, 최신순
CREATE INDEX idx_notices_pinned_create ON notices (is_pinned DESC, created_at DESC);
-- 특정 회원의 제재 이력 조회
CREATE INDEX idx_sanctions_member ON member_sanctions (member_id, created_at DESC);
-- 활성 제재 조회 (자동 해제 배치/로그인 차단 체크용)
CREATE INDEX idx_sanctions_active ON member_sanctions (is_active, end_at);
-- 관리자별 활동 이력 조회
CREATE INDEX idx_admin_logs_admin_date ON admin_logs (admin_id, created_at DESC);
-- 행동 유형별 로그 조회 (삭제 로그 필터링용)
CREATE INDEX idx_admin_logs_action ON admin_logs (action, created_at DESC);

-- 아르바이트 Q&A 게시판 특성상 많은양의 데이터와 데이터 삽입/갱신/삭제 보다 조회가 더 많이 일어나기에 이를 고려하여
-- 인덱스 설정으로 가장 빈번하게 일어나는 조건의 탐색방법을 Full Table Scan이 아닌 내부적으로 B-Tree 구조로 탐색하도록 하여 조회 성능을 향상