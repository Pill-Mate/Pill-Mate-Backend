package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


//v2
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EfcyDto {
    //약물 이름
    @JsonProperty("itemName")
    private String itemName;

    //약물 식별 번호
    @JsonProperty("itemSeq")
    private String itemSeq;

    //효능
    @JsonProperty("className")
    private String className;

    //약물종류
    @JsonProperty("effectName")
    private String effectName;

    //회사이름
    @JsonProperty("entpName")
    private String entpName;

    //약물 이미지
    @JsonProperty("image")
    private String image;

    @Builder
    public EfcyDto(String itemName, String itemSeq, String className, String effectName, String entpName) {
        this.itemName = itemName;
        this.itemSeq = itemSeq;
        this.className = className;
        this.effectName = effectName;
        this.entpName = entpName;
        this.image = null;
    }
}
