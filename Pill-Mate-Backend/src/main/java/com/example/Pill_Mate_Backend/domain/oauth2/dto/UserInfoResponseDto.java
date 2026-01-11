package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserInfoResponseDto", description = "사용자 프로필 응답 DTO")
public class UserInfoResponseDto {

    @Schema(description = "사용자 이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String profileImage;

    @Schema(description = "사용자 이메일", example = "honggildong@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
