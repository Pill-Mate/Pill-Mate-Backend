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
        String username,
        String pharmacyName,
        String pharmacyPhone,
        String hospitalName,
        String hospitalPhone,
        String identifyNumber,
        String medicineName,
        String ingredient,
        IngredientUnit ingredientUnit,
        float ingredientAmount,

        URI medicineImage,
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
