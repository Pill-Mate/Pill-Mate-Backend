package com.example.Pill_Mate_Backend.domain.register.dto;

import java.awt.*;

public record CrashResponseDTO(
        String crashName,
        String medicineName,
        String ingredient,
        Image image
) {
}
