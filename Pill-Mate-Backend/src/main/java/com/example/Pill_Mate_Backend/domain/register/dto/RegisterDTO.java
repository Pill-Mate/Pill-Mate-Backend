package com.example.Pill_Mate_Backend.domain.register.dto;

import com.example.Pill_Mate_Backend.CommonEntity.enums.*;
import lombok.Builder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Builder
public record RegisterDTO (
        //String username, -> 헤더에 토큰 보내는 것으로 함
        String pharmacyName,
        String pharmacyPhone,
        String pharmacyAddress,
        String hospitalName,
        String hospitalPhone,
        String hospitalAddress,
        String identifyNumber,
        String medicineName,
        String ingredient,
        IngredientUnit ingredientUnit,
        float ingredientAmount,

        URI medicineImage,
        String entpName,
        String classname,
        String efficacy,
        String sideEffect,
        String caution,
        String storage,
        Long medicineId,
        Set<IntakeCount> intakeCounts,
        Set<IntakeFrequency> intakeFrequencys,
        MealUnit mealUnit,
        int mealTime,
        EatUnit eatUnit,
        int eatCount,
        OffsetDateTime startDate,
        int intakePeriod,
        float medicineVolume,
        boolean isAlarm,
       Set<String> cautionTypes
) {
}
