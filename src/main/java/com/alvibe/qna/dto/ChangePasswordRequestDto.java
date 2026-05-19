package com.alvibe.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequestDto {// 마이페이지의 비밀번호 변경
    @Size(min=4, message = "비밀번호는 최소 4자 이상이어야 합니다")
    @NotBlank
    private String password;

    @Size(min=4, message = "비밀번호는 최소 4자 이상이어야 합니다")
    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;
}