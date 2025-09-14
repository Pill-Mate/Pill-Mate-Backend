package com.example.Pill_Mate_Backend.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmScheduleDTO {

    @NotNull
    @Schema(description = "유저 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull
    @Schema(description = "복용 날짜", example = "2025-08-23", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate intakeDate;

    @NotNull
    @Schema(description = "복용 시간", example = "08:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime intakeTime;
}
