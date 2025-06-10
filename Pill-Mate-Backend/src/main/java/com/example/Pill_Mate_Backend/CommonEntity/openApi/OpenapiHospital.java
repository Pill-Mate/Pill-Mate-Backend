package com.example.Pill_Mate_Backend.CommonEntity.openApi;
import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpenapiHospital extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "duty_name")
    private String dutyName;

    @Column(name = "duty_addr")
    private String dutyAddr;

    @Column(name = "duty_tel")
    private String dutyTel;

    @Column(name = "duty_emcls_name")
    private String dutyEmclsName;

    @Column(name = "duty_time_mon")
    private String dutyTimeMon;

    private Double latitude;

    private Double longitude;
}
