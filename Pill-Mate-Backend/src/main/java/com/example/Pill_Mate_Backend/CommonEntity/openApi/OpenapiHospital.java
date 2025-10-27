package com.example.Pill_Mate_Backend.CommonEntity.openApi;
import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "openapi_hospital")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpenapiHospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "병원 고유 ID", example = "1")
    private Integer id;

    @Column(name = "duty_name")
    @Schema(description = "병원 이름", example = "서울중앙병원")
    private String dutyName;

    @Column(name = "duty_addr")
    @Schema(description = "병원 주소", example = "서울특별시 강남구 테헤란로 123")
    private String dutyAddr;

    @Column(name = "duty_tel")
    @Schema(description = "대표 전화번호", example = "02-123-4567")
    private String dutyTel;

    @Column(name = "duty_emcls_name")
    @Schema(description = "응급실 여부", example = "응급실운영")
    private String dutyEmclsName;

    @Column(name = "duty_time_mon")
    @Schema(description = "월요일 진료시간", example = "0900~1800")
    private String dutyTimeMon;

    @Schema(description = "병원 위도", example = "37.123456")
    private Double latitude;

    @Schema(description = "병원 경도", example = "127.123456")
    private Double longitude;
}