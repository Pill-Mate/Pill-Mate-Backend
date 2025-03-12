package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoSignUpDTO {
    private String kakaoAccessToken; // 프론트에서 전달한 액세스 토큰
    private String fcmToken;

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public void setKakaoAccessToken(String kakaoAccessToken) {
        this.kakaoAccessToken = kakaoAccessToken;
    }

}
