package com.example.Pill_Mate_Backend.CommonEntity.openApi;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dur_product_info")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DurProductInfo extends BaseEntity {

    @Id
    @Column(name = "item_seq")
    @Schema(description = "의약품 품목 일련번호", example = "198601234")
    private String itemSeq;

    @Column(name = "item_name", length = 500)
    @Schema(description = "의약품명", example = "타이레놀정 500mg")
    private String itemName;

    @Column(name = "entp_name")
    @Schema(description = "제약사명", example = "한국얀센")
    private String entpName;

    @Column(name = "item_permit_date")
    @Schema(description = "품목허가일자", example = "2010-03-25")
    private String itemPermitDate;

    @Column(name = "etc_otc_code")
    @Schema(description = "전문/일반 의약품 코드", example = "일반의약품")
    private String etcOtcCode;

    @Column(name = "class_no")
    @Schema(description = "의약품 분류번호", example = "219")
    private String classNo;

    @Column(name = "chart", columnDefinition = "TEXT")
    @Schema(description = "성상 (약의 외형 정보)", example = "흰색의 타원형 정제")
    private String chart;

    @Column(name = "bar_code")
    @Schema(description = "바코드", example = "8801234567890")
    private String barCode;

    @Column(name = "material_name", columnDefinition = "TEXT")
    @Schema(description = "주성분명", example = "아세트아미노펜 500mg")
    private String materialName;

    @Column(name = "ee_doc_id", columnDefinition = "TEXT")
    @Schema(description = "효능 효과 문서 ID", example = "EE12345")
    private String eeDocId;

    @Column(name = "ud_doc_id", columnDefinition = "TEXT")
    @Schema(description = "용법 용량 문서 ID", example = "UD54321")
    private String udDocId;

    @Column(name = "nb_doc_id", columnDefinition = "TEXT")
    @Schema(description = "주의사항 문서 ID", example = "NB99887")
    private String nbDocId;

    @Column(name = "insert_file", columnDefinition = "TEXT")
    @Schema(description = "첨부문서 파일 경로", example = "/files/drug/file1234.pdf")
    private String insertFile;

    @Column(name = "storage_method", columnDefinition = "TEXT")
    @Schema(description = "보관방법", example = "실온(1~30℃) 보관")
    private String storageMethod;

    @Column(name = "valid_term")
    @Schema(description = "유효기간", example = "제조일로부터 36개월")
    private String validTerm;

    @Column(name = "pack_unit", columnDefinition = "TEXT")
    @Schema(description = "포장단위", example = "500정/병")
    private String packUnit;

    @Column(name = "edi_code")
    @Schema(description = "보험코드 (EDI)", example = "650100123")
    private String ediCode;

    @Column(name = "cancel_name")
    @Schema(description = "취소 여부", example = "취소")
    private String cancelName;

    @Column(name = "type_code")
    @Schema(description = "의약품 구분 코드", example = "C")
    private String typeCode;

    @Column(name = "type_name")
    @Schema(description = "약물 주의", example = "임부금기,용량주의,노인주의,첨가제주의")
    private String typeName;

    @Column(name = "change_date")
    @Schema(description = "변경일자", example = "2022-12-01")
    private String changeDate;

    @Column(name = "bizrno")
    @Schema(description = "사업자등록번호", example = "1101112345")
    private String bizrno;
}