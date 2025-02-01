package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingDTO {
    private Boolean alarmMarketing;
    private Boolean alarmInfo;
    private Time wakeupTime;
    private Time bedTime;
    private Time morningTime;
    private Time lunchTime;
    private Time dinnerTime;

}
