package com.example.Pill_Mate_Backend.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    @NotNull
    @Schema(description = "알림 날짜", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-23")
    private LocalDate notifyDate;

    @NotNull
    @Schema(description = "알림 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "08:30:00")
    private LocalTime notifyTime;

    @NotBlank
    @Schema(description = "알림 제목", requiredMode = Schema.RequiredMode.REQUIRED, example = "복약 알림")
    private String title;

    @NotBlank
    @Schema(description = "알림 내용", requiredMode = Schema.RequiredMode.REQUIRED, example = "약을 복용할 시간입니다.")
    private String content;
}
