package com.alvibe.qna.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateMyInfoRequestDto { // 마이페이지의 내 정보 수정
    @Email
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank
    @Size(min=2, max=10, message = "닉네임은 2자에서 10자 사이로 작성해주세요")
    private String nickname;
}
