package com.example.Pill_Mate_Backend.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationIdDTO {

    @NotNull
    @Schema(description = "알림 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long notificationId;
}
