package com.alvibe.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequestDto {// 마이페이지의 비밀번호 변경
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 영문과 숫자만 포함한 8자 이상이어야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 영문과 숫자만 포함한 8자 이상이어야 합니다."
    )
    private String newPassword;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String newPasswordConfirm;
}