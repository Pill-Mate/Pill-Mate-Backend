package com.example.Pill_Mate_Backend.domain.register.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "HospitalResponseDTO", description = "병원 정보 응답 DTO")
public class HospitalResponseDTO {

     @Schema(description = "병원 주소", example = "서울특별시 강남구 테헤란로 123")
     private String dutyAddr;

     @Schema(description = "병원 이름", example = "서울병원")
     private String dutyName;

     @Schema(description = "대표 전화번호", example = "02-123-4567")
     private String dutyTel;
}

