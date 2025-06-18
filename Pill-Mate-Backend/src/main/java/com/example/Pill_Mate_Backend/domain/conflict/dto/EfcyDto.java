package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


//v2
@AllArgsConstructor
@Builder
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

    @JsonProperty("entpName")
    private String entpName;
}
