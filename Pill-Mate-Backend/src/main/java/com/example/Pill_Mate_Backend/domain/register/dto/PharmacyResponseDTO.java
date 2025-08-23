package com.example.Pill_Mate_Backend.domain.register.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PharmacyResponseDTO {
    String address;   // 병원 주소
    String name;   // 병원 이름
    String phone;   // 대표 전화번호
}
