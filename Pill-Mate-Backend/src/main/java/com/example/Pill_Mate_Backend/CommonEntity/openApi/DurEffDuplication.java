package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "dur_eff_duplication")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DurEffDuplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dur_seq")
    private String durSeq;

    @Column(name = "effect_name")
    private String effectName;

    @Column(name = "ingr_code")
    private String ingrCode;

    @Column(name = "ingr_name")
    private String ingrName;

    @Column(name = "ingr_eng_name")
    private String ingrEngName;

    @Column(name = "item_seq")
    private String itemSeq;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "entp_name")
    private String entpName;

    @Column(name = "sers_name")
    private String sersName;

    @Column(name = "class_name")
    private String className;

    @Column(name = "notification_date")
    private LocalDate notificationDate;

    @Column(name = "change_date")
    private LocalDate changeDate;
}

