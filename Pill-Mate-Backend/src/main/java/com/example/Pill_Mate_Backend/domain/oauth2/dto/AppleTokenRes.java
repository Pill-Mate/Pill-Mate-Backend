package com.example.Pill_Mate_Backend.domain.oauth2.dto;

public record AppleTokenRes(
        String access_token,
        String refresh_token,
        String id_token,
        Long expires_in,
        String token_type
) {}