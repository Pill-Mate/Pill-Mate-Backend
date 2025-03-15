package com.example.Pill_Mate_Backend.domain.management.dto;


import com.example.Pill_Mate_Backend.CommonEntity.enums.EatUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IngredientUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Builder
public record ManagementDetailDto (
        //Medicine
        String identifyNumber,
        @Schema(description = "약물 이름")
        String medicineName,
        String ingredient,
        float ingredientAmount,

        URI medicineImage,
        @Schema(description = "제약 회사명")
        String entpName,

        @Schema(description = "약물 분류명")
        String className,

        @Schema(description = "약물 id")
        Long medicineId,


        //User
        //Set<Time> intakeTimes,
        @Schema(description = "기상 시간")
        LocalTime wakeupTime,
        @Schema(description = "아침 시간")
        LocalTime morningTime,
        @Schema(description = "점심 시간")
        LocalTime lunchTime,
        @Schema(description = "저녁 시간")
        LocalTime dinnerTime,
        @Schema(description = "취침 시간")
        LocalTime bedTime,

        //Shedule
        @Schema(description = "복용 횟수(아점저공취)")
        Set<String> intakeCounts,
        @Schema(description = "섭취 요일")
        Set<String> intakeFrequencys,
        @Schema(description = "식전, 식후 선택")
        MealUnit mealUnit,
        @Operation(description = "식전후 시간")
        int mealTime,
        @Schema(description = "정(개), 캡슐, ml, 포, 주사")
        EatUnit eatUnit,
        @Schema(description = "1회 투약량")
        int eatCount,
        @Schema(description = "복약 시작일")
        LocalDate startDate,
        @Schema(description = "복약일수")
        int intakePeriod,
        @Schema(description = "1회 투여 용량(선택), 0.45")
        float medicineVolume,
        @Schema(description = " MG, MCG, ML, G, PERCENT, SKIP")
        IngredientUnit ingredientUnit,
        @Schema(description = "알람 선택 여부")
        boolean isAlarm
) {
}
