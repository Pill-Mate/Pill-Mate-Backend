package com.example.Pill_Mate_Backend.CommonEntity.openApi;
import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "dur_taboo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DurTaboo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "자동 증가 ID", example = "1")
    private Integer id;

    @Column(name = "dur_seq")
    @Schema(description = "DUR 고유 일련번호", example = "202300045")
    private String durSeq;

    @Column(name = "ingr_kor_name")
    @Schema(description = "성분명(한글)", example = "아세트아미노펜")
    private String ingrKorName;

    @Column(name = "item_seq")
    @Schema(description = "품목 일련번호", example = "198601234")
    private String itemSeq;

    @Column(name = "item_name", length = 500)
    @Schema(description = "의약품명", example = "타이레놀정 500mg")
    private String itemName;

    @Column(name = "entp_name")
    @Schema(description = "제약사명", example = "한국얀센")
    private String entpName;

    @Column(name = "mix_item_name")
    @Schema(description = "병용금기 대상 약품명", example = "이부프로펜정")
    private String mixItemName;

    @Column(name = "mix_entp_name")
    @Schema(description = "병용금기 대상 제약사명", example = "동아제약")
    private String mixEntpName;

    @Column(name = "prohbt_content", columnDefinition = "TEXT")
    @Schema(description = "병용금기 상세 내용", example = "해당 약물과 병용 시 간독성 위험이 증가할 수 있음")
    private String prohbtContent;

    @Column(name = "notification_date")
    @Schema(description = "공고일자", example = "2023-08-15")
    private LocalDate notificationDate;

    @Column(name = "mixture_item_seq")
    @Schema(description = "병용약품 식별번호", example = "198601235")
    private String mixtureItemSeq;

    @Column(name = "class_name")
    @Schema(description = "약물 분류명", example = "진통해열제")
    private String className;
}