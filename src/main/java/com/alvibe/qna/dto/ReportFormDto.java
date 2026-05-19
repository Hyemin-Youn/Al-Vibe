package com.alvibe.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportFormDto {

    @NotBlank(message = "신고 유형을 선택해주세요.")
    private String reasonCategory;

    private String reason;
}