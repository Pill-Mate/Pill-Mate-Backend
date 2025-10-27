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
@Table(name = "drug_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugDetail extends BaseEntity {

    @Id
    @Column(name = "item_seq")
    @Schema(description = "품목 일련번호", example = "198601234")
    private Long itemSeq;

    @Column(name = "efcy_qesitm", columnDefinition = "TEXT")
    @Schema(description = "효능·효과", example = "이 약은 고혈압 치료에 사용됩니다.")
    private String efcyQesitm;

    @Column(name = "use_method_qesitm", columnDefinition = "TEXT")
    @Schema(description = "사용법", example = "1일 1회, 1회 1정 식후 복용")
    private String useMethodQesitm;

    @Column(name = "atpn_qesitm", columnDefinition = "TEXT")
    @Schema(description = "주의사항", example = "임산부나 수유부는 복용 전 의사와 상담하세요.")
    private String atpnQesitm;

    @Column(name = "intrc_qesitm", columnDefinition = "TEXT")
    @Schema(description = "상호작용", example = "다른 고혈압 약물과 병용 시 혈압이 급격히 낮아질 수 있음")
    private String intrcQesitm;

    @Column(name = "se_qesitm", columnDefinition = "TEXT")
    @Schema(description = "부작용", example = "어지러움, 두통, 구역질 등이 나타날 수 있음")
    private String seQesitm;

    @Column(name = "deposit_method", columnDefinition = "TEXT")
    @Schema(description = "저장 방법", example = "실온(1~30도) 보관, 직사광선 피함")
    private String depositMethod;

    @Column(name = "open_de")
    @Schema(description = "공개일자", example = "2022-12-01T10:30:00")
    private LocalDateTime openDe;

    @Column(name = "update_de")
    @Schema(description = "수정일자", example = "2023-04-15")
    private LocalDate updateDe;

//    @Column(name = "updated_at")
//    @Schema(description = "마지막 수정일 (시스템 기준)", example = "2023-04-20T14:10:00")
//    private LocalDateTime updatedAt;
}
