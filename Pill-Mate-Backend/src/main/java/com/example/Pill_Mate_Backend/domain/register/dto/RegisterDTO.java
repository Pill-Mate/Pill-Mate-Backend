package com.example.Pill_Mate_Backend.domain.register.dto;

import com.example.Pill_Mate_Backend.CommonEntity.enums.EatUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IngredientUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import lombok.Builder;

import java.net.URI;
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
        Set<String> intakeCounts,
        Set<String> intakeFrequencys,
        MealUnit mealUnit,
        int mealTime,
        EatUnit eatUnit,
        int eatCount,
        Date startDate,
        int intakePeriod,
        float medicineVolume,
        boolean isAlarm,
       Set<String> cautionTypes
) {
}
