package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickMedicineDTO {
    private Long medicineScheduleId;
}