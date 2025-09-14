package com.example.Pill_Mate_Backend.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFcmTokenDTO {

    @NotBlank
    @Schema(description = "등록할 FCM 토큰", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "eCyFHN-4Rm-HhMcbGP4JQE:APA91bEeqjAxZj6...")
    private String fcmToken;
}
