package com.example.Pill_Mate_Backend.domain.conflict.dto;

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
//    private String itemSeq;
//    private String itemName;
//    private String materialName;

    private List<UsjntTabooApiItem> usjntTabooList;  // 병용금기 약 목록
    private List<EfcyDplctApiItem> efcyDplctList;    // 효능군중복 약 목록
    private List<MedicineConflict> conflictWithUserMeds; // 내 약들과 충돌한 목록
}
