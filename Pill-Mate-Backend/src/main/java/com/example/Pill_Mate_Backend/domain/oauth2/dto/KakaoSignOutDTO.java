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
@Schema(name = "KakaoSignOutDTO", description = "카카오 로그아웃 요청 DTO")
public class KakaoSignOutDTO {

    @Schema(
            description = "프론트엔드에서 전달받은 카카오 액세스 토큰",
            example = "kakao-access-token-example-1234567890",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String kakaoAccessToken;
}
