package com.alvibe.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnswerFormDto {

    @NotBlank(message = "답변 내용을 입력해주세요")
    @Size(min = 5, message = "답변을 최소 5자 이상 입력해주세요.")
    private String content;
}
