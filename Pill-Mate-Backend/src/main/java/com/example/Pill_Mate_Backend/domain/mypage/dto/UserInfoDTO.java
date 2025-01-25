package com.example.Pill_Mate_Backend.domain.mypage.dto;

import lombok.Builder;
import lombok.Data;

import java.net.URI;

@Builder
@Data
public class UserInfoDTO {
    private String userName;
    private String email;
    private String profileImage;

    public UserInfoDTO(String username, String email, String profileImage) {
        this.userName = username;
        this.email = email;
        this.profileImage = profileImage;
    }
}
