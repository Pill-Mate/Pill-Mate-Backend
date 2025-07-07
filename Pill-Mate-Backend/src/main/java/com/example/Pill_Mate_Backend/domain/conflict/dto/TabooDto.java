package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@Data
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TabooDto {
    //약물 이름
    private String mixItemName;
    //약물 식별 번호
    @JsonProperty("mixtureItemSeq")
    private String mixtureItemSeq;
    // 약물 병용 금기 사유
    @JsonProperty("prohbtContent")
    private String prohbtContent;

    //약물 분류명
    @JsonProperty("className")
    private String className;

    // 약물 회사명
    @JsonProperty("entpName")
    private String entpName;

    //약물 이미지
    @JsonProperty("image")
    private String image;

    public TabooDto(String mixItemName, String mixtureItemSeq, String prohbtContent, String className, String entpName) {
        this.mixItemName = mixItemName;
        this.mixtureItemSeq = mixtureItemSeq;
        this.prohbtContent = prohbtContent;
        this.className = className;
        this.entpName = entpName;
        this.image = null;
    }
}