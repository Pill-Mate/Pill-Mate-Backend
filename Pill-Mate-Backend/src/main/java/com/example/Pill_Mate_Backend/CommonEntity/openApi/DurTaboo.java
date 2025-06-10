package com.example.Pill_Mate_Backend.CommonEntity.openApi;
import com.example.Pill_Mate_Backend.global.common.BaseEntity;
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
    private Integer id;

    @Column(name = "dur_seq")
    private String durSeq;

    @Column(name = "ingr_kor_name")
    private String ingrKorName;

    @Column(name = "ingr_eng_name")
    private String ingrEngName;

    @Column(name = "item_seq")
    private String itemSeq;

    @Column(name = "item_name", length = 500)
    private String itemName;

    @Column(name = "entp_name")
    private String entpName;

    @Column(name = "mix_ingr_kor_name")
    private String mixIngrKorName;

    @Column(name = "mix_ingr_eng_name")
    private String mixIngrEngName;

    @Column(name = "mix_item_name")
    private String mixItemName;

    @Column(name = "mix_entp_name")
    private String mixEntpName;

    @Column(name = "prohbt_content", columnDefinition = "TEXT")
    private String prohbtContent;

    @Column(name = "notification_date")
    private LocalDate notificationDate;
}
