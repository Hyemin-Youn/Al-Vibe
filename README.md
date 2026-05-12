# Al-Vibe 현재 프로젝트 구조

---

## 📁 전체 프로젝트 구조

```
qna/
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ com/example/qna/
│     │     ├─ QnaApplication.java
│     │     ├─ config/
│     │     ├─ controller/
│     │     ├─ service/
│     │     ├─ repository/
│     │     ├─ entity/
│     │     ├─ dto/
│     │     └─ exception/
│     └─ resources/
│        ├─ static/
│        ├─ templates/
│        ├─ application.properties
│        ├─ data.sql
│        └─ schema.sql
└─ pom.xml
```

---

## 1. Java 패키지 구조

**기본 패키지:** `com.example.qna`
**경로:** `src/main/java/com/example/qna`

```
com/example/qna
├─ QnaApplication.java           # 애플리케이션 진입점 (main 메서드)
│
├─ config/                       # 설정 클래스
│  └─ SecurityConfig.java
│
├─ controller/                   # 요청 처리 계층 (Presentation Layer)
│  ├─ MainController.java
│  ├─ MemberController.java
│  ├─ QuestionController.java
│  ├─ AnswerController.java
│  └─ MyPageController.java
│
├─ service/                      # 비즈니스 로직 계층 (Service Layer)
│  ├─ MemberService.java
│  ├─ QuestionService.java
│  ├─ AnswerService.java
│  └─ MyPageService.java
│
├─ repository/                   # 데이터 접근 계층 (Persistence Layer)
│  ├─ MemberRepository.java
│  ├─ QuestionRepository.java
│  └─ AnswerRepository.java
│
├─ entity/                       # JPA 엔티티 (DB 테이블 매핑)
│  ├─ Member.java
│  ├─ Question.java
│  └─ Answer.java
│
├─ dto/                          # 데이터 전송 객체
│  ├─ MemberFormDto.java
│  ├─ LoginFormDto.java
│  ├─ QuestionFormDto.java
│  ├─ AnswerFormDto.java
│  └─ QuestionSearchDto.java
│
└─ exception/                    # 예외 처리
   └─ GlobalExceptionHandler.java
```

### 계층별 역할 요약

| 패키지 | 역할 |
|---|---|
| `config` | Spring Security 등 빈(Bean) 설정 |
| `controller` | HTTP 요청 매핑 및 뷰 반환 |
| `service` | 비즈니스 로직 및 트랜잭션 처리 |
| `repository` | Spring Data JPA를 통한 DB 연동 |
| `entity` | DB 테이블과 매핑되는 도메인 객체 |
| `dto` | 계층 간 데이터 전달용 객체 |
| `exception` | 전역 예외 처리 (`@ControllerAdvice`) |

---

## 2. Thymeleaf 템플릿 구조

**기본 위치:** `src/main/resources/templates`

```
templates/
├─ layout/                       # 공통 레이아웃 (fragment)
│  ├─ base.html
│  ├─ navbar.html
│  └─ footer.html
│
├─ main/                         # 메인 페이지
│  └─ index.html
│
├─ member/                       # 회원 관련 페이지
│  ├─ signup.html
│  ├─ login.html
│  └─ mypage.html
│
├─ question/                     # 질문 관련 페이지
│  ├─ list.html
│  ├─ detail.html
│  ├─ form.html
│  └─ edit.html
│
├─ answer/                       # 답변 관련 페이지
│  └─ edit.html
│
└─ error/                        # 에러 페이지
   ├─ 403.html
   ├─ 404.html
   └─ 500.html
```

---

## 3. 정적 파일 구조

**기본 위치:** `src/main/resources/static`

```
static/
├─ css/
│  └─ style.css                  # 공통 스타일
├─ js/
│  └─ common.js                  # 공통 스크립트
└─ images/                       # 이미지 리소스
```

---

## 4. 설정 파일 구조

**기본 위치:** `src/main/resources`

```
resources/
├─ application.properties        # Spring Boot 환경 설정
├─ schema.sql                    # DB 스키마 (테이블 생성 등)
└─ data.sql                      # 초기 데이터 삽입 스크립트
```

---

## 🧱 아키텍처 흐름

```
Client (Browser)
     │
     ▼
[ Controller ] ──→ [ Service ] ──→ [ Repository ] ──→ [ DB ]
     │                  │                  │
     └── DTO ───────────┘                  │
                                           │
                                       [ Entity ]
                                           │
                                           ▼
                                      [ Thymeleaf View ]
```

요청은 **Controller → Service → Repository** 순으로 전달되며,
계층 간 데이터 이동은 **DTO**를 활용해 Entity 노출을 방지합니다.