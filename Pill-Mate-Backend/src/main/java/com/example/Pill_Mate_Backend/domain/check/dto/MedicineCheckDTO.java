package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.*;

import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCheckDTO {
    private Long medicineScheduleId;
    private Boolean eatCheck;

}
