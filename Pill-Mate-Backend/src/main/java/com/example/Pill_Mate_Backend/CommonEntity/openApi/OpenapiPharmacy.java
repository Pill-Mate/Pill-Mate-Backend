package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pharmacy")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpenapiPharmacy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String hpid;

    private String name;

    private String address;

    private String phone;

    @Column(name = "time_mon")
    private String timeMon;

    private Double lat;

    private Double lon;
}
