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
@Table(name = "drug_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugDetail extends BaseEntity {

    @Id
    @Column(name = "item_seq")
    private Long itemSeq;

    @Column(name = "efcy_qesitm", columnDefinition = "TEXT")
    private String efcyQesitm;

    @Column(name = "use_method_qesitm", columnDefinition = "TEXT")
    private String useMethodQesitm;

    @Column(name = "atpn_qesitm", columnDefinition = "TEXT")
    private String atpnQesitm;

    @Column(name = "intrc_qesitm", columnDefinition = "TEXT")
    private String intrcQesitm;

    @Column(name = "se_qesitm", columnDefinition = "TEXT")
    private String seQesitm;

    @Column(name = "deposit_method", columnDefinition = "TEXT")
    private String depositMethod;

    @Column(name = "open_de")
    private LocalDateTime openDe;

    @Column(name = "update_de")
    private LocalDate updateDe;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
