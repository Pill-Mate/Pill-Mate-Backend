package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "dur_eff_duplication")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DurEffDuplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "PK (자동 생성 ID)", example = "1")
    private Integer id;

    @Column(name = "dur_seq")
    @Schema(description = "DUR 고유 식별자", example = "DUR0000421")
    private String durSeq;

    @Column(name = "effect_name")
    @Schema(description = "중복 효능 이름", example = "혈압강하제")
    private String effectName;

    @Column(name = "ingr_code")
    @Schema(description = "성분 코드", example = "120100ATB")
    private String ingrCode;

    @Column(name = "ingr_name")
    @Schema(description = "성분명 (한글)", example = "암로디핀")
    private String ingrName;

    @Column(name = "ingr_eng_name")
    @Schema(description = "성분명 (영문)", example = "Amlodipine")
    private String ingrEngName;

    @Column(name = "item_seq")
    @Schema(description = "품목 일련번호", example = "198601234")
    private Long itemSeq;

    @Column(name = "item_name")
    @Schema(description = "의약품 이름", example = "암로디핀정 5mg")
    private String itemName;

    @Column(name = "entp_name")
    @Schema(description = "제약사 이름", example = "한국유나이티드제약")
    private String entpName;

    @Column(name = "sers_name")
    @Schema(description = "약효군 이름", example = "칼슘채널 차단제")
    private String sersName;

    @Column(name = "class_name")
    @Schema(description = "약물 분류명", example = "순환계용약")
    private String className;

    @Column(name = "notification_date")
    @Schema(description = "고시일자", example = "2022-09-01")
    private LocalDate notificationDate;

    @Column(name = "change_date")
    @Schema(description = "변경일자", example = "2023-01-15")
    private LocalDate changeDate;
}