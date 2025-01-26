package com.example.Pill_Mate_Backend.domain.mypage.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.sql.Time;

@Builder
@Data
public class RoutineDTO {
    private Time wakeupTime;
    private Time bedTime;
    private Time morningTime;
    private Time lunchTime;
    private Time dinnerTime;

    public RoutineDTO(Time wakeupTime, Time bedTime, Time morningTime, Time lunchTime, Time dinnerTime) {
        this.wakeupTime = wakeupTime;
        this.bedTime = bedTime;
        this.morningTime = morningTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
    }
}
