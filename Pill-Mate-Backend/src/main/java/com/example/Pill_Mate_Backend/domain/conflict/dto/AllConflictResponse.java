package com.example.Pill_Mate_Backend.domain.conflict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllConflictResponse {

    @Schema(description = "병용금기 약 목록")
    private List<TabooDto> usjntTabooList;

    @Schema(description = "효능군 중복 약 목록")
    private List<EfcyDto> efcyDplctList;

    @Schema(description = "사용자가 복용 중인 약들과 충돌한 목록")
    private MedicineConflict conflictWithUserMeds;
}
