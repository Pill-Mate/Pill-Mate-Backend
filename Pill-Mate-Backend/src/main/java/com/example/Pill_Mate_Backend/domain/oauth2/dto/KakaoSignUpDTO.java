package com.example.Pill_Mate_Backend.domain.oauth2.dto;

public class KakaoSignUpDTO {
    private String kakaoAccessToken; // 프론트에서 전달한 액세스 토큰
    private Boolean marketingAlarm;

    // 기본 생성자, getter, setter
    public KakaoSignUpDTO() {}

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public void setKakaoAccessToken(String kakaoAccessToken) {
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public Boolean getMarketingAlarm() {
        return marketingAlarm;
    }
    public void setMarketingAlarm(Boolean marketingAlarm) {
        this.marketingAlarm = marketingAlarm;
    }
}
