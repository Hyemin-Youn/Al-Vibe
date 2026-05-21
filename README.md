# 알바이브 (Al-Vibe)

> 아르바이트에 대한 정보를 원하는 사람들을 위한 Q&A 커뮤니티 플랫폼

---

## 📌 프로젝트 소개

아르바이트와 관련된 궁금증을 질문하고 답변받을 수 있는 Q&A 커뮤니티 웹 서비스입니다.
카테고리별 질문 작성, 답변 채택, 대댓글, 신고 기능 등을 제공합니다.

---

## 👥 팀원 소개

| 이름   | 담당                                          |
| ------ | --------------------------------------------- |
| 김민규 | 회원 기능 (회원가입, 로그인, 마이페이지)      |
| 황순혁 | 질문 기능 (질문 CRUD, 검색, 예외 처리 페이지) |
| 박삼령 | 답변 기능 (답변 CRUD, 채택, 대댓글, 신고)     |
| 박채린 | 검색/필터링/정렬/카테고리                     |
| 이성준 | UI/레이아웃/Bootstrap                         |
| 박국현 | DB/통합 테스트                                |
| 윤혜민 | DB/통합 테스트                                |

---

## 🛠 기술 스택

| 분류         | 기술                                               |
| ------------ | -------------------------------------------------- |
| **Backend**  | Spring Boot 4.0.6, Spring Security, QueryDSL 5.1.0 |
| **Frontend** | Thymeleaf, Bootstrap 5                             |
| **Database** | MySQL 8.4                                          |
| **ORM**      | JPA (Hibernate 7.2)                                |
| **Build**    | Maven                                              |
| **형상관리** | GitHub                                             |

---

## ⚙️ 실행 방법

### 1. DB 설정

MySQL Workbench 또는 CLI에서 아래 순서로 실행:

```sql
source Al_Vibe_schema.sql       -- 테이블 생성
source Al_Vibe_sample_data.sql  -- 샘플 데이터 삽입
```

### 2. .env 파일 생성

프로젝트 루트에 `.env` 파일 생성 후 아래 내용 입력:

```
DB_URL=jdbc:mysql://localhost:3306/al_vibe?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
DB_USERNAME=root
DB_PASSWORD=비밀번호입력
JPA_DDL_AUTO=
```

### 3. 빌드 및 실행

```
mvn clean compile
QnaApplication.java 실행
http://localhost:8080 접속
```

---

## 📁 프로젝트 구조

```
Al-Vibe/
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ com/alvibe/qna/
│     │     ├─ QnaApplication.java
│     │     │
│     │     ├─ config/
│     │     │  ├─ JpaAuditingConfig.java
│     │     │  ├─ QuerydslConfig.java
│     │     │  └─ SecurityConfig.java
│     │     │
│     │     ├─ controller/
│     │     │  ├─ AnswerController.java
│     │     │  ├─ GlobalController.java
│     │     │  ├─ MainController.java
│     │     │  ├─ MemberController.java
│     │     │  ├─ MyPageController.java
│     │     │  ├─ QuestionController.java
│     │     │  └─ ReportController.java
│     │     │
│     │     ├─ dto/
│     │     │  ├─ AnswerFormDto.java
│     │     │  ├─ ChangePasswordRequestDto.java
│     │     │  ├─ LoginFormDto.java
│     │     │  ├─ MemberSignupDto.java
│     │     │  ├─ MyPageProfileDto.java
│     │     │  ├─ QuestionFormDto.java
│     │     │  ├─ ReportFormDto.java
│     │     │  └─ UpdateMyInfoRequestDto.java
│     │     │
│     │     ├─ entity/
│     │     │  ├─ Answer.java
│     │     │  ├─ BaseTimeEntity.java
│     │     │  ├─ Category.java
│     │     │  ├─ Member.java
│     │     │  ├─ MemberRole.java      (enum)
│     │     │  ├─ MemberStatus.java    (enum)
│     │     │  ├─ Question.java
│     │     │  └─ Report.java
│     │     │
│     │     ├─ exception/
│     │     │  └─ GlobalExceptionHandler.java
│     │     │
│     │     ├─ repository/
│     │     │  ├─ AnswerRepository.java
│     │     │  ├─ AnswerRepositoryCustom.java
│     │     │  ├─ AnswerRepositoryCustomImpl.java
│     │     │  ├─ CategoryRepository.java
│     │     │  ├─ MemberRepository.java
│     │     │  ├─ QuestionRepository.java
│     │     │  └─ ReportRepository.java
│     │     │
│     │     └─ service/
│     │        ├─ AnswerService.java
│     │        ├─ MemberService.java
│     │        ├─ MyPageService.java
│     │        ├─ QuestionService.java
│     │        └─ ReportService.java
│     │
│     └─ resources/
│        ├─ static/
│        │  └─ css/
│        │     └─ style.css
│        │
│        ├─ templates/
│        │  ├─ answer/
│        │  │  └─ list.html
│        │  ├─ error/
│        │  │  ├─ 403.html
│        │  │  ├─ 404.html
│        │  │  └─ 500.html
│        │  ├─ layout/
│        │  │  ├─ base.html
│        │  │  ├─ footer.html
│        │  │  └─ navbar.html
│        │  ├─ main/
│        │  │  └─ index.html
│        │  ├─ member/
│        │  │  ├─ login.html
│        │  │  ├─ mypage.html
│        │  │  └─ signup.html
│        │  └─ question/
│        │     ├─ detail.html
│        │     ├─ edit.html
│        │     ├─ form.html
│        │     └─ list.html
│        │
│        ├─ Al_Vibe_schema.sql
│        ├─ Al_Vibe_sample_data.sql
│        └─ application.properties
│
├─ .env
└─ pom.xml
```

## 1. Java 패키지 구조

**기본 패키지:** `com.alvibe.qna`
**경로:** `src/main/java/com/alvibe/qna`

```
com/alvibe/qna
├─ QnaApplication.java                   # 애플리케이션 진입점 (main 메서드)
│
├─ config/                               # 설정 클래스
│  ├─ JpaAuditingConfig.java             # JPA Auditing 설정
│  ├─ QuerydslConfig.java                # QueryDSL JPAQueryFactory 빈 등록
│  └─ SecurityConfig.java               # Spring Security 설정
│
├─ controller/                           # 요청 처리 계층 (Presentation Layer)
│  ├─ AnswerController.java              # 답변/대댓글 CRUD, 채택
│  ├─ GlobalController.java             # 전역 컨트롤러
│  ├─ MainController.java               # 메인 페이지 (/ → /questions/list)
│  ├─ MemberController.java             # 회원가입, 로그인
│  ├─ MyPageController.java             # 마이페이지, 정보 수정, 비밀번호 변경
│  ├─ QuestionController.java           # 질문 CRUD, 검색, 페이징
│  └─ ReportController.java             # 신고 접수
│
├─ dto/                                  # 데이터 전송 객체
│  ├─ AnswerFormDto.java                 # 답변/대댓글 작성·수정 폼
│  ├─ ChangePasswordRequestDto.java     # 비밀번호 변경 요청
│  ├─ LoginFormDto.java                 # 로그인 폼
│  ├─ MemberSignupDto.java              # 회원가입 폼
│  ├─ MyPageProfileDto.java             # 마이페이지 프로필 데이터
│  ├─ QuestionFormDto.java              # 질문 작성·수정 폼
│  ├─ ReportFormDto.java                # 신고 폼
│  └─ UpdateMyInfoRequestDto.java       # 내 정보 수정 요청
│
├─ entity/                               # JPA 엔티티 (DB 테이블 매핑)
│  ├─ Answer.java                        # 답변 (대댓글 자기참조 구조)
│  ├─ BaseTimeEntity.java               # 공통 시간 필드 (createdAt, updatedAt)
│  ├─ Category.java                     # 카테고리
│  ├─ Member.java                       # 회원
│  ├─ MemberRole.java                   # 회원 권한 Enum (USER, ADMIN)
│  ├─ MemberStatus.java                 # 회원 상태 Enum (ACTIVE, LOCK, LEAVE)
│  ├─ Question.java                     # 질문
│  └─ Report.java                       # 신고
│
├─ exception/                            # 예외 처리
│  └─ GlobalExceptionHandler.java       # 전역 예외 처리 (@ControllerAdvice)
│
├─ repository/                           # 데이터 접근 계층 (Persistence Layer)
│  ├─ AnswerRepository.java             # 답변 기본 JPA Repository
│  ├─ AnswerRepositoryCustom.java       # QueryDSL 커스텀 인터페이스
│  ├─ AnswerRepositoryCustomImpl.java   # QueryDSL 커스텀 구현체
│  ├─ CategoryRepository.java           # 카테고리 Repository
│  ├─ MemberRepository.java             # 회원 Repository
│  ├─ QuestionRepository.java           # 질문 Repository
│  └─ ReportRepository.java             # 신고 Repository
│
└─ service/                              # 비즈니스 로직 계층 (Service Layer)
   ├─ AnswerService.java                 # 답변/대댓글 비즈니스 로직
   ├─ MemberService.java                # 회원 비즈니스 로직, UserDetailsService 구현
   ├─ MyPageService.java                # 마이페이지 비즈니스 로직
   ├─ QuestionService.java              # 질문 비즈니스 로직
   └─ ReportService.java                # 신고 비즈니스 로직
```

---

### 계층별 역할

| 패키지       | 역할                                         |
| ------------ | -------------------------------------------- |
| `config`     | Spring Security, QueryDSL, JPA Auditing 설정 |
| `controller` | HTTP 요청 매핑 및 뷰 반환                    |
| `service`    | 비즈니스 로직 및 트랜잭션 처리               |
| `repository` | Spring Data JPA 및 QueryDSL 기반 DB 연동     |
| `entity`     | DB 테이블과 매핑되는 도메인 객체             |
| `dto`        | 계층 간 데이터 전달용 객체                   |
| `exception`  | 전역 예외 처리 (`@ControllerAdvice`)         |

---

## 2. Thymeleaf 템플릿 구조

**기본 위치:** `src/main/resources/templates`

```
templates/
├─ answer/                       # 답변 관련
│  └─ list.html                  # 답변 목록 (Fragment 방식)
│
├─ error/                        # 에러 페이지
│  ├─ 403.html                   # 권한 없음
│  ├─ 404.html                   # 페이지 없음
│  └─ 500.html                   # 서버 오류
│
├─ layout/                       # 공통 레이아웃 (Fragment)
│  ├─ base.html                  # 기본 레이아웃
│  ├─ footer.html                # 푸터
│  └─ navbar.html                # 네비게이션 바
│
├─ main/                         # 메인 페이지
│  └─ index.html                 # 메인 (→ /questions/list 리다이렉트)
│
├─ member/                       # 회원 관련 페이지
│  ├─ login.html                 # 로그인
│  ├─ mypage.html                # 마이페이지
│  └─ signup.html                # 회원가입
│
└─ question/                     # 질문 관련 페이지
   ├─ detail.html                # 질문 상세
   ├─ edit.html                  # 질문 수정
   ├─ form.html                  # 질문 작성
   └─ list.html                  # 질문 목록
```

---

## 3. 정적 파일 구조

**기본 위치:** `src/main/resources/static`

```
static/
└─ css/
   └─ style.css                  # 공통 스타일
```

---

## 4. 설정 파일 구조

**기본 위치:** `src/main/resources`

```
resources/
├─ application.properties        # Spring Boot 환경 설정
├─ Al_Vibe_schema.sql            # DB 스키마 (테이블 생성)
└─ Al_Vibe_sample_data.sql       # 초기 샘플 데이터 삽입
```

---

## 🧱 아키텍처 흐름

```
Client (Browser)
     │
     ▼
[ Controller ] ──→ [ Service ] ──→ [ Repository ] ──→ [ DB ]
     │                  │                  │
     └── DTO ───────────┘              [ Entity ]
                                           │
                                           ▼
                                   [ Thymeleaf View ]
```

---

## 💡 주요 기능

### 회원

- 이메일 기반 회원가입 / 로그인 / 로그아웃
- 마이페이지 (프로필 수정, 비밀번호 변경, 활동 내역)

### 질문

- 카테고리별 질문 작성 / 수정 / 삭제
- 제목 / 내용 / 제목+내용 검색
- 최신순 / 조회수순 / 미답변순 정렬
- 페이징 처리

### 답변

- 답변 작성 / 수정 / 삭제
- 답변 채택 및 채택 취소 (질문 작성자만 가능)
- 본인 답변 채택 불가
- 대댓글 작성 / 수정 / 삭제

### 신고

- 답변 / 질문 신고 접수
- 신고 유형 분류 (욕설·비방 / 스팸·광고 / 잘못된 정보 / 불법 내용 / 기타)
- 중복 신고 방지
- 본인 답변/질문 신고 불가
- 기타 선택 시 상세 사유 필수 입력
