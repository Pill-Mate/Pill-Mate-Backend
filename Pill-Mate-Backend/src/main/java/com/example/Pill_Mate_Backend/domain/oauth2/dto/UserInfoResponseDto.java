package com.example.Pill_Mate_Backend.domain.oauth2.dto;

public class UserInfoResponseDto {
    private String name;

    private String profileImage;
    private String email;
    private Boolean alarmMarketing;

    public UserInfoResponseDto(String name, String profileImage, String email, boolean alarmMarketing) {
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
        this.alarmMarketing = alarmMarketing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAlarmMarketing() {
        return alarmMarketing;
    }

    public void setAlarmMarketing(Boolean alarmMarketing) {
        this.alarmMarketing = alarmMarketing;
    }
}
