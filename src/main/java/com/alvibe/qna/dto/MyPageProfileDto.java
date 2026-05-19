package com.alvibe.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyPageProfileDto { // 프로필 카드 영역 데이터
    private String nickname;
    private String email;
    private LocalDateTime createAt;
}