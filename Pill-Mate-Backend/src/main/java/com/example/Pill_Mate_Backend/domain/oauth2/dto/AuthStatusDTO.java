package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthStatusDTO {
    private boolean isLoggedIn;
    private boolean isOnboarded;
}