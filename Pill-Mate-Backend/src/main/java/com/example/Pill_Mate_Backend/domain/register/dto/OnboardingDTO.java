package com.example.Pill_Mate_Backend.domain.register.dto;

import java.sql.Time;

public record OnboardingDTO(

    Time morningTime,
    Time lunchTime,
    Time dinnerTime,
    Time wakeupTime,
    Time bedTime,
    boolean alarmMarketing
)
{}
