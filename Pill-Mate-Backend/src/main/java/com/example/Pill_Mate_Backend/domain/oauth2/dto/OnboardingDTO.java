package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;

@Builder
@Data
public class OnboardingDTO {
    private Boolean alarmMarketing;
    private Time wakeupTime;
    private Time bedTime;
    private Time morningTime;
    private Time lunchTime;
    private Time dinnerTime;

}
