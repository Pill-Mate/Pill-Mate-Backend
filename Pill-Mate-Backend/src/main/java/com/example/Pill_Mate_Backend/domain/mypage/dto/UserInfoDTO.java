package com.example.Pill_Mate_Backend.domain.mypage.dto;

import lombok.Builder;
import lombok.Data;

import java.net.URI;

@Builder
@Data
public class UserInfoDTO {
    private String username;
    private String email;
    private URI profileImage;

    public UserInfoDTO(String username, String email, URI profileImage) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }
}
