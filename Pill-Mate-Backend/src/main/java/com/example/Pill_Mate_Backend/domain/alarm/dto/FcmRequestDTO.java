package com.example.Pill_Mate_Backend.domain.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmRequestDTO {
    private String deviceToken;
    private String title;
    private String body;
}
