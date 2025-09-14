package com.example.Pill_Mate_Backend.domain.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTitleDTO {

    @NotNull
    @Schema(description = "알림 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
    private Long notificationId;

    @NotNull
    @Schema(description = "알림 날짜", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-08-23")
    private LocalDate notifyDate;

    @NotNull
    @Schema(description = "알림 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "08:30:00")
    private LocalTime notifyTime;

    @NotBlank
    @Schema(description = "알림 제목", requiredMode = Schema.RequiredMode.REQUIRED, example = "복약 알림")
    private String title;

    @Schema(description = "읽음 여부", example = "false")
    private boolean notificationRead;

    @JsonProperty("isFcm")
    @Schema(description = "FCM 알림 여부", example = "true")
    private boolean isFcm;
}
