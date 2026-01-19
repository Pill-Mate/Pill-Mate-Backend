package com.example.Pill_Mate_Backend.domain.management.dto;

import com.example.Pill_Mate_Backend.CommonEntity.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record ManagementDetailDto(
        @Schema(description = "식별 번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "200901187")
        Long itemSeq,

        @Schema(description = "약물 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "이지에스정(방기황기탕건조엑스)")
        String medicineName,

        @Schema(description = "성분명", requiredMode = Schema.RequiredMode.REQUIRED, example = "분홍색의 원형 필름코팅정")
        String ingredient,

        @Schema(description = "성분량", requiredMode = Schema.RequiredMode.REQUIRED, example = "500")
        Float ingredientAmount,

        @Schema(description = "약물 이미지 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/154333330132500115")
        String medicineImage,

        @Schema(description = "제약 회사명", requiredMode = Schema.RequiredMode.REQUIRED, example = "해열.진통.소염제")
        String entpName,

        @Schema(description = "약물 분류명", requiredMode = Schema.RequiredMode.REQUIRED, example = "해열.진통.소염제")
        String className,

        @Schema(description = "약물 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Long medicineId,

        @Schema(description = "복용 시간 목록", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"07:40:00\", \"12:30:00\", \"19:40:00\", \"21:00:00\"]")
        List<LocalTime> intakeTimes,

        @Schema(description = "기상 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "06:00:00")
        LocalTime wakeupTime,

        @Schema(description = "아침 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "09:00:00")
        LocalTime morningTime,

        @Schema(description = "점심 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "12:00:00")
        LocalTime lunchTime,

        @Schema(description = "저녁 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "20:00:00")
        LocalTime dinnerTime,

        @Schema(description = "취침 시간", requiredMode = Schema.RequiredMode.REQUIRED, example = "21:00:00")
        LocalTime bedTime,

        @Schema(description = "복용 횟수(아점저공취)", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"MORNING\", \"DINNER\"]")
        List<IntakeCount> intakeCounts,

        @Schema(description = "섭취 요일", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
        List<IntakeFrequency> intakeFrequencys,

        @Schema(description = "식전/식후 구분", requiredMode = Schema.RequiredMode.REQUIRED, example = "MEALBEFORE")
        MealUnit mealUnit,

        @Schema(description = "식전후 시간(분)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
        int mealTime,

        @Schema(description = "복용 단위(정, 캡슐 등)", requiredMode = Schema.RequiredMode.REQUIRED, example = "JUNG")
        EatUnit eatUnit,

        @Schema(description = "1회 투약량", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
        int eatCount,

        @Schema(description = "복약 시작일", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-04-20T00:00:00+09:00")
        OffsetDateTime startDate,

        @Schema(description = "복약 기간(일수)", requiredMode = Schema.RequiredMode.REQUIRED, example = "14")
        int intakePeriod,

        @Schema(description = "1회 투여 용량", requiredMode = Schema.RequiredMode.REQUIRED, example = "0.75")
        float medicineVolume,

        @Schema(description = "성분 단위(MG, MCG 등)", requiredMode = Schema.RequiredMode.REQUIRED, example = "MG")
        IngredientUnit ingredientUnit,

        @Schema(description = "알람 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
        boolean isAlarm
) {}
