package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "drug_basic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugBasic extends BaseEntity {

    @Id
    @Column(name = "item_seq")
    private Long itemSeq;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "entp_seq")
    private Long entpSeq;

    @Column(name = "entp_name")
    private String entpName;

    @Column(name = "drug_shape")
    private String drugShape;

    @Column(name = "color_class1")
    private String colorClass1;

    @Column(name = "chart", columnDefinition = "TEXT")
    private String chart;

    @Column(name = "item_image", columnDefinition = "TEXT")
    private String itemImage;

    @Column(name = "etc_otc_name")
    private String etcOtcName;

    @Column(name = "class_name")
    private String className;

    @Column(name = "form_code_name")
    private String formCodeName;

    @Column(name = "change_date")
    private LocalDate changeDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
