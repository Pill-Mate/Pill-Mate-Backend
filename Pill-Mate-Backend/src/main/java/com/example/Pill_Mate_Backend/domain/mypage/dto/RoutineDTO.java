package com.example.Pill_Mate_Backend.domain.mypage.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.time.LocalTime;

@Builder
@Data
public class RoutineDTO {
    private LocalTime wakeupTime;
    private LocalTime bedTime;
    private LocalTime morningTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;

    public RoutineDTO(LocalTime wakeupTime, LocalTime bedTime, LocalTime morningTime, LocalTime lunchTime, LocalTime dinnerTime) {
        this.wakeupTime = wakeupTime;
        this.bedTime = bedTime;
        this.morningTime = morningTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
    }
}
