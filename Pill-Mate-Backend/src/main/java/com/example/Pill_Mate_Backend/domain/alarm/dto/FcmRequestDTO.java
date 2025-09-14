package com.example.Pill_Mate_Backend.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmRequestDTO {

    @NotBlank
    @Schema(description = "푸시를 받을 기기의 FCM 토큰", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "eCyFHN-4Rm-HhMcbGP4JQE:APA91bEeqjAxZj6...")
    private String deviceToken;

    @NotBlank
    @Schema(description = "푸시 알림 제목", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "복약 알림")
    private String title;

    @NotBlank
    @Schema(description = "푸시 알림 내용", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "약을 복용할 시간입니다.")
    private String body;
}
