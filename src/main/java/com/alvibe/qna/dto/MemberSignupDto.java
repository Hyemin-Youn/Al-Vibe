package com.alvibe.qna.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class MemberSignupDto {
    @Email
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @Size(min=4, message = "비밀번호는 최소 4자 이상이어야 합니다")
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    @Size(min=2, max=10, message = "닉네임은 2자에서 10자 사이로 작성해주세요")
    private String nickname;

    @AssertTrue(message = "비밀번호가 일치하여야 합니다")
    public boolean isValidPassword(){
        return Objects.equals(this.getPassword(), this.getPasswordConfirm());
    }


}