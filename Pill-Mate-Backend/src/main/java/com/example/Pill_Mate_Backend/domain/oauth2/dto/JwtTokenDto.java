package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "JwtTokenDto", description = "JWT 토큰 응답 DTO")
public class JwtTokenDto {

        @Schema(description = "Access Token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private String accessToken;

        @Schema(description = "Refresh Token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private String refreshToken;
}
