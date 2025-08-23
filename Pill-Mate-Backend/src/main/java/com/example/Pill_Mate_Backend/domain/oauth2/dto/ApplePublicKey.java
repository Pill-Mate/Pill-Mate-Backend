package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record ApplePublicKey (String kty,
                              String kid,
                              String alg,
                              String n,
                              String e) {
}