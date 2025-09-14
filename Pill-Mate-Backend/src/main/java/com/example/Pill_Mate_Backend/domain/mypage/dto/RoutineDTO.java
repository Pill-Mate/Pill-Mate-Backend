package com.example.Pill_Mate_Backend.domain.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RoutineDTO", description = "개인 루틴 조회 DTO")
@Builder
@Data
public class RoutineDTO {
    @Schema(description = "기상 시간", example = "07:30", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime wakeupTime;

    @Schema(description = "취침 시간", example = "23:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime bedTime;

    @Schema(description = "아침 식사 시간", example = "08:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime morningTime;

    @Schema(description = "점심 식사 시간", example = "12:30", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime lunchTime;

    @Schema(description = "저녁 식사 시간", example = "19:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime dinnerTime;

}
