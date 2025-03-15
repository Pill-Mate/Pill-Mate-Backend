package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EfcyDplctApiItem {
    //약물 이름
    @JsonProperty("ITEM_NAME")
    private String itemName;

    //약물 식별 번호
    @JsonProperty("ITEM_SEQ")
    private String itemSeq;

    //효능
    @JsonProperty("CLASS_NAME")
    private String className;

    //약물종류
    @JsonProperty("EFFECT_NAME")
    private String effectName;

    @JsonProperty("ENTP_NAME")
    private String entpName;
}
