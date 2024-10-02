package com.example.Pill_Mate_Backend.domain.register.dto;

import lombok.Builder;

import java.awt.*;
import java.time.LocalDate;

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
        Image image,
        String classname,
        String efficacy,
        String sideEffect,
        String caution,
        String storage,
        Long medicineId,
        String intakeFrequency,
        String intakeCount,
        String mealUnit,
        int mealTime,
        String intake1Unit,
        int intake1Count,
        LocalDate startDate,
        int intakePeriod,
        String medicineUnit,
        float medicineVolume,
        boolean isAlarm
        //cautionTypes -> 타입 어떻게
) {
}
