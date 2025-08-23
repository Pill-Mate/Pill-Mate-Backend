package com.example.Pill_Mate_Backend.domain.register.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record OnboardingDTO(

        @Schema(description = "아침 시간", example = "08:00:00")
        LocalTime morningTime,

        @Schema(description = "점심 시간", example = "12:30:00")
        LocalTime lunchTime,

        @Schema(description = "저녁 시간", example = "19:00:00")
        LocalTime dinnerTime,

        @Schema(description = "기상 시간", example = "07:00:00")
        LocalTime wakeupTime,

        @Schema(description = "취침 시간", example = "23:00:00")
        LocalTime bedTime,

        @Schema(description = "마케팅 알람 동의 여부", example = "true")
        boolean alarmMarketing

) {}
