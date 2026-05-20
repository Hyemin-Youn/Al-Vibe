package com.alvibe.qna.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyPageProfileDto { // 프로필 카드 영역 데이터
    private Long Id;
    private String nickname;
    private String email;
    private LocalDateTime createAt;
}