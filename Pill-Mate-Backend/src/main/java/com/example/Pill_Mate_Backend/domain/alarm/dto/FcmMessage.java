package com.example.Pill_Mate_Backend.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {

    @Schema(description = "검증 전송 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean validateOnly;

    @NotNull
    @Schema(description = "메시지 본문", requiredMode = Schema.RequiredMode.REQUIRED)
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {

        @NotNull
        @Schema(description = "알림 정보", requiredMode = Schema.RequiredMode.REQUIRED)
        private Notification notification;

        @NotNull
        @Schema(description = "FCM 토큰", requiredMode = Schema.RequiredMode.REQUIRED, example = "eCyFHN-4Rm-HhMcbGP4JQE:APA91b...")
        private String token;

        @Schema(description = "안드로이드 옵션", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Android android;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {

        @NotNull
        @Schema(description = "알림 제목", requiredMode = Schema.RequiredMode.REQUIRED, example = "복용 알림")
        private String title;

        @NotNull
        @Schema(description = "알림 내용", requiredMode = Schema.RequiredMode.REQUIRED, example = "약을 복용할 시간입니다.")
        private String body;

        // @Schema(description = "이미지 URL")
        // private String image;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Android {

        @Schema(description = "우선순위 (high or normal)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "high")
        private String priority;
    }
}
