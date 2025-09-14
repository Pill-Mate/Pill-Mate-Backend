package com.example.Pill_Mate_Backend.domain.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "AlarmMarketingDTO", description = "마케팅 알림 수신 여부 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmMarketingDTO {

    @Schema(description = "마케팅 알림 수신 동의 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "alarmMarketing은 필수 값입니다.")
    private Boolean alarmMarketing;
}
