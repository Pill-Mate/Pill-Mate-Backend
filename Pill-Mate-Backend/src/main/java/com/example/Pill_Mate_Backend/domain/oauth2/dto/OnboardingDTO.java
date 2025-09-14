package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "OnboardingDTO", description = "회원 온보딩 정보 DTO")
public class OnboardingDTO {

    @Schema(description = "마케팅 알림 수신 동의 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean alarmMarketing;

    @Schema(description = "서비스/정보 알림 수신 동의 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean alarmInfo;

    @Schema(description = "기상 시간", example = "07:30", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime wakeupTime;

    @Schema(description = "취침 시간", example = "23:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime bedTime;

    @Schema(description = "아침 식사 시간", example = "08:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime morningTime;

    @Schema(description = "점심 식사 시간", example = "12:30", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime lunchTime;

    @Schema(description = "저녁 식사 시간", example = "19:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dinnerTime;
}
