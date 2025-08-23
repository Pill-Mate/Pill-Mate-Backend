package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingDTO {
    private Boolean alarmMarketing;
    private Boolean alarmInfo;
    private LocalTime wakeupTime;
    private LocalTime bedTime;
    private LocalTime morningTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;

}
