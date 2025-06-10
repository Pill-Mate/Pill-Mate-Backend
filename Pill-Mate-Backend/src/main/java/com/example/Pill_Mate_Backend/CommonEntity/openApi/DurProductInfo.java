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
@Table(name = "dur_product_info")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DurProductInfo extends BaseEntity {

    @Id
    @Column(name = "item_seq")
    private String itemSeq;

    @Column(name = "item_name", length = 500)
    private String itemName;

    @Column(name = "entp_name")
    private String entpName;

    @Column(name = "item_permit_date")
    private String itemPermitDate;

    @Column(name = "etc_otc_code")
    private String etcOtcCode;

    @Column(name = "class_no")
    private String classNo;

    @Column(name = "chart", columnDefinition = "TEXT")
    private String chart;

    @Column(name = "bar_code")
    private String barCode;

    @Column(name = "material_name", columnDefinition = "TEXT")
    private String materialName;

    @Column(name = "ee_doc_id", columnDefinition = "TEXT")
    private String eeDocId;

    @Column(name = "ud_doc_id", columnDefinition = "TEXT")
    private String udDocId;

    @Column(name = "nb_doc_id", columnDefinition = "TEXT")
    private String nbDocId;

    @Column(name = "insert_file", columnDefinition = "TEXT")
    private String insertFile;

    @Column(name = "storage_method", columnDefinition = "TEXT")
    private String storageMethod;

    @Column(name = "valid_term")
    private String validTerm;

    @Column(name = "pack_unit", columnDefinition = "TEXT")
    private String packUnit;

    @Column(name = "edi_code")
    private String ediCode;

    @Column(name = "cancel_name")
    private String cancelName;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "change_date")
    private String changeDate;

    @Column(name = "bizrno")
    private String bizrno;
}
