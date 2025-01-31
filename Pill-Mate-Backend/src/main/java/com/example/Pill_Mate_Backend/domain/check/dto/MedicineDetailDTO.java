package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDetailDTO {
    private String medicineName;
    private URI medicineImage;
    private String className;
    private String ingredient;
    private String efficacy;
    private String caution;
    private String sideEffect;
    private String storage;
    private String entpName;
}
