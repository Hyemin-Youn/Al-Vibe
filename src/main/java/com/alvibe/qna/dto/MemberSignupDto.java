package com.alvibe.qna.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class MemberSignupDto {
    @Email
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 영문과 숫자만 포함한 8자 이상이어야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min=2, max=10, message = "닉네임은 2자에서 10자 사이로 작성해주세요")
    private String nickname;

    @AssertTrue(message = "비밀번호가 일치하여야 합니다")
    public boolean isValidPassword(){
        return Objects.equals(this.getPassword(), this.getPasswordConfirm());
    }


}
