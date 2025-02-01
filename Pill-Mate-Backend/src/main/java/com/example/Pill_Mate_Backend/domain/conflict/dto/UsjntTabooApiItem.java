package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsjntTabooApiItem {
    //약물 이름
    @JsonProperty("MIXTURE_ITEM_NAME")
    private String mixtureItemName;
    //약물 식별 번호
    @JsonProperty("MIXTURE_ITEM_SEQ")
    private String mixtureItemSeq;
    // 약물 병용 금기 사유
    @JsonProperty("PROHBT_CONTENT")
    private String prohbtContent;

    //약물 분류명
    @JsonProperty("CLASS_NAME")
    private String className;

    // 약물 회사명
    @JsonProperty("ENTP_NAME")
    private String entpName;
}