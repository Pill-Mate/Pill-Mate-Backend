package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;


@Schema(description = "낱알 식별 약물 기본 정보 (공공 API)")
@Entity
@Table(name = "drug_basic")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DrugBasic extends BaseEntity {

    @Id
    @Schema(description = "품목 일련번호", example = "200502123")
    @Column(name = "item_seq")
    private Long itemSeq;

    @Schema(description = "제품명", example = "타이레놀정500밀리그램(수출용)")
    @Column(name = "item_name")
    private String itemName;

    @Schema(description = "업체 일련번호", example = "123456")
    @Column(name = "entp_seq")
    private Long entpSeq;

    @Schema(description = "업체명", example = "한국얀센(주)")
    @Column(name = "entp_name")
    private String entpName;

    @Schema(description = "약물 모양", example = "장방형")
    @Column(name = "drug_shape")
    private String drugShape;

    @Schema(description = "색상 분류", example = "하양")
    @Column(name = "color_class1")
    private String colorClass1;

    @Schema(description = "약물 성상", example = "흰색의 장방형 정제")
    @Column(name = "chart", columnDefinition = "TEXT")
    private String chart;

    @Schema(description = "제품 이미지 URL", example = "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/12345678")
    @Column(name = "item_image", columnDefinition = "TEXT")
    private String itemImage;

    @Schema(description = "일반/전문 의약품 구분", example = "일반의약품")
    @Column(name = "etc_otc_name")
    private String etcOtcName;

    @Schema(description = "분류명", example = "해열·진통·소염제")
    @Column(name = "class_name")
    private String className;

    @Schema(description = "제형 코드명", example = "정제")
    @Column(name = "form_code_name")
    private String formCodeName;

    @Schema(description = "변경 일자", example = "2024-06-01")
    @Column(name = "change_date")
    private LocalDate changeDate;

//    @Schema(description = "최종 업데이트 일시", example = "2025-09-28T15:00:00")
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
}