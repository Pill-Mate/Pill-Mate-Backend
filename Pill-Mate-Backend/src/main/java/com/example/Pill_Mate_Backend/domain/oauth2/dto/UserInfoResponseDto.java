package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import java.net.URI;

public class UserInfoResponseDto {
    private String name;
    private URI profileImage;
    private String email;


    public UserInfoResponseDto(String name, URI profileImage, String email) {
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(URI profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
