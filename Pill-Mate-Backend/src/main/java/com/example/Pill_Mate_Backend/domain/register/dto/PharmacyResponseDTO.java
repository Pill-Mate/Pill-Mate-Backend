package com.example.Pill_Mate_Backend.domain.register.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "PharmacyResponseDTO", description = "약국 정보 응답 DTO")
public class PharmacyResponseDTO {

    @Schema(description = "약국 주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "약국 이름", example = "행복약국")
    private String name;

    @Schema(description = "약국 대표 전화번호", example = "02-987-6543")
    private String phone;
}
