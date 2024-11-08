package com.example.Pill_Mate_Backend.domain.oauth2.dto;

public record  JwtTokenDto (
        String accessToken,
        String refreshToken) {

}