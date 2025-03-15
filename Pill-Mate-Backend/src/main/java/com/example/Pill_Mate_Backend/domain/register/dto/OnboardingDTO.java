package com.example.Pill_Mate_Backend.domain.register.dto;

import java.sql.Time;
import java.time.LocalTime;

public record OnboardingDTO(

    LocalTime morningTime,
    LocalTime lunchTime,
    LocalTime dinnerTime,
    LocalTime wakeupTime,
    LocalTime bedTime,
    boolean alarmMarketing
)
{}
