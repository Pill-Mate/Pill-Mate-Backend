package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SignUpDTO", description = "회원가입 응답 DTO")
public class SignUpDTO {

    @Schema(description = "JWT Access Token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String jwtToken;

    @Schema(description = "JWT Refresh Token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    @Schema(description = "로그인 성공 여부",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean login;
}
