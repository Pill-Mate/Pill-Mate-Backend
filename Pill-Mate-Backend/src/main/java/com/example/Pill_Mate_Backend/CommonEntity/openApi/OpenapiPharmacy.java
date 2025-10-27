package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "openapi_pharmacy")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpenapiPharmacy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "약국 고유 ID", example = "1")
    private Integer id;

    @Column(unique = true)
    @Schema(description = "약국 식별자(HPID)", example = "A1234567")
    private String hpid;

    @Schema(description = "약국명", example = "중앙약국")
    private String name;

    @Schema(description = "약국 주소", example = "서울특별시 종로구 종로1가 1")
    private String address;

    @Schema(description = "대표 전화번호", example = "02-987-6543")
    private String phone;

    @Column(name = "time_mon")
    @Schema(description = "월요일 영업시간", example = "0900~2000")
    private String timeMon;

    @Schema(description = "약국 위도", example = "37.567891")
    private Double lat;

    @Schema(description = "약국 경도", example = "126.987654")
    private Double lon;
}
