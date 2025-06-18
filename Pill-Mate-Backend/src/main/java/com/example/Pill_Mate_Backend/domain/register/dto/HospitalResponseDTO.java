package com.example.Pill_Mate_Backend.domain.register.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HospitalResponseDTO {
     String dutyAddr;   // 병원 주소
     String dutyName;   // 병원 이름
     String dutyTel;   // 대표 전화번호
}
