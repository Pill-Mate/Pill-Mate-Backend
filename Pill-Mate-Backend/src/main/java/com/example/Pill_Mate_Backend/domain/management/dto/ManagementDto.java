package com.example.Pill_Mate_Backend.domain.management.dto;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

public class ManagementDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CurrentPillResponseDto {

        @Schema(description = "복용 중인 약물 수",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2")
        private Integer pillCount;

        @Schema(description = "복용 중인 약물 목록",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private List<CurrentPillResponse> currentPillResponseList;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "현재 복용 중인 약물")
    public static class CurrentPillResponse {

        @Schema(description = "복용 시작 날짜",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2025-04-20")
        private LocalDate startDate;

        @Schema(description = "복용 종료 날짜",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2025-05-04")
        private LocalDate endDate;

        @Schema(description = "약물 분류명",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "해열·진통·소염제")
        private String className;

        @Schema(description = "복용 기간 (일)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "14")
        private Integer intakePeriod;

        @Schema(description = "약물 이름",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "이지에스정(방기황기탕건조엑스)")
        private String medicineName;

        @Schema(description = "제조사명",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "한국제약")
        private String entpName;

        @Schema(description = "약물 사진 URI",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string", format = "uri",
                example = "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/154333330132500115")
        private URI image;

        @Schema(description = "스케줄 ID",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "1")
        private Long scheduleId;

        public static CurrentPillResponse from(Schedule schedule) {
            return CurrentPillResponse.builder()
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getStartDate().plusDays(schedule.getIntakePeriod() - 1))
                    .className(schedule.getMedicine().getClassName())
                    .medicineName(schedule.getMedicine().getMedicineName())
                    .intakePeriod(schedule.getIntakePeriod())
                    .entpName(schedule.getMedicine().getEntpName())
                    .image(schedule.getMedicine().getMedicineImage())
                    .scheduleId(schedule.getId())
                    .build();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "복용 중단된 약물")
    public static class StopPillResponse {

        @Schema(description = "복용 시작 날짜",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2025-04-20")
        private LocalDate startDate;

        @Schema(description = "복용 종료 날짜",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2025-05-04")
        private LocalDate endDate;

        @Schema(description = "약물 분류명",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "해열·진통·소염제")
        private String className;

        @Schema(description = "복용 기간 (일)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "14")
        private Integer intakePeriod;

        @Schema(description = "약물 이름",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "이지에스정(방기황기탕건조엑스)")
        private String medicineName;

        @Schema(description = "제조사명",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "한국제약")
        private String entpName;

        @Schema(description = "약물 사진 URI",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string", format = "uri",
                example = "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/154333330132500115")
        private URI image;

        @Schema(description = "스케줄 ID",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "1")
        private Long scheduleId;

        public static StopPillResponse from(Schedule schedule) {
            return StopPillResponse.builder()
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getStartDate().plusDays(schedule.getIntakePeriod() - 1))
                    .className(schedule.getMedicine().getClassName())
                    .medicineName(schedule.getMedicine().getMedicineName())
                    .intakePeriod(schedule.getIntakePeriod())
                    .entpName(schedule.getMedicine().getEntpName())
                    .image(schedule.getMedicine().getMedicineImage())
                    .scheduleId(schedule.getId())
                    .build();
        }
    }
}
