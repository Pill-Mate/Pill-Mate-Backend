package com.example.Pill_Mate_Backend.domain.alarm.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlarmScheduleDTO {
    private Long userId;
    private LocalDate intakeDate;
    private LocalTime intakeTime;
}
