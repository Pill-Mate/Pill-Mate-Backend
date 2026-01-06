package com.example.Pill_Mate_Backend.domain.register.dto;

import com.example.Pill_Mate_Backend.CommonEntity.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;
@Builder
public record RegisterDTO (
        @Schema(description = "약국 이름", example = "Test Pharmacy")
        String pharmacyName,

        @Schema(description = "약국 전화번호", example = "010-1234-5678")
        String pharmacyPhone,

        @Schema(description = "약국 주소", example = "서울특별시 강남구 약국로 1길")
        String pharmacyAddress,

        @Schema(description = "병원 이름", example = "Test Hospital")
        String hospitalName,

        @Schema(description = "병원 전화번호", example = "02-123-4567")
        String hospitalPhone,

        @Schema(description = "병원 주소", example = "서울특별시 강남구 병원로 2길")
        String hospitalAddress,

        @Schema(description = "검색한 약물인지 직접입력한 약물인지 - ex) API, CUSTOM", example = "CUSTOM")
        SourceType sourceType,

        @Schema(description = "약물 식별 번호", example = "A123456")
        Long itemSeq,


        //nullable
        @Schema(description = "약물 이름", example = "Painkiller")
        String medicineName,

        //nullable
        @Schema(description = "약물 성분", example = "Ibuprofen")
        String ingredient,

        @Schema(description = "1개 성분량 단위 - ex) MG, MCG, ML, G, PERCENT, SKIP", example = "MG")
        IngredientUnit ingredientUnit,

        @Schema(description = "1회 성분량", example = "200.0")
        float ingredientAmount,

        //nullable
        @Schema(description = "약물 이미지", example = "https://example.com/medicine.jpg")
        URI medicineImage,

        //nullable
        @Schema(description = "회사이름", example = "Pillmate Inc.")
        String entpName,

        //nullable
        @Schema(description = "분류명", example = "Analgesic")
        String classname,

        //nullable
        @Schema(description = "효능", example = "Reduces pain and fever")
        String efficacy,

        //nullable
        @Schema(description = "부작용", example = "Nausea")
        String sideEffect,

        //nullable
        @Schema(description = "주의사항", example = "Do not take on an empty stomach")
        String caution,

        //nullable
        @Schema(description = "보관 방법", example = "Store in a cool, dry place")
        String storage,

        @Schema(description = "아침/점심/저녁/공복/취침전", example = "[\"MORNING\", \"DINNER\"]")
        Set<IntakeCount> intakeCounts,

        @Schema(description = "월화수목금토일", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
        Set<IntakeFrequency> intakeFrequencys,

        @Schema(description = "식전/식후 선택", example = "MEALBEFORE")
        MealUnit mealUnit,

        @Schema(description = "식전후 시간 선택", example = "30")
        int mealTime,

        @Schema(description = "1회 투약 단위", example = "JUNG")
        EatUnit eatUnit,

        @Schema(description = "1회 투약량", example = "1")
        int eatCount,

        @Schema(description = "복용 시작일", example = "2024-10-30T09:00:00+09:00")
        OffsetDateTime startDate,

        @Schema(description = "복용 일 수", example = "10")
        int intakePeriod,

        @Schema(description = "1회 투여용량", example = "500.0")
        float medicineVolume,

        @Schema(description = "알람 유무", example = "true")
        boolean isAlarm,

        @Schema(description = "API 타입 이름 (노인주의, 첨가제 주의 등)", example = "[\"PREGNANT\", \"AGE\"]")
        Set<String> cautionTypes
) {}
