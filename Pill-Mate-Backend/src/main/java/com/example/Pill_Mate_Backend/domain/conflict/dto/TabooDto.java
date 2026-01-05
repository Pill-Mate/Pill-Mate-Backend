package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TabooDto {

    @JsonProperty("mixItemName")
    @Schema(description = "약물 이름", example = "아스피린정100mg")
    private String mixItemName;

    @JsonProperty("mixtureItemSeq")
    @Schema(description = "약물 식별 번호", example = "201101234")
    private Long mixtureItemSeq;

    @JsonProperty("prohbtContent")
    @Schema(description = "병용 금기 사유", example = "와파린과 병용 시 출혈 위험 증가")
    private String prohbtContent;

    @JsonProperty("className")
    @Schema(description = "약물 분류명", example = "해열·진통·소염제")
    private String className;

    @JsonProperty("entpName")
    @Schema(description = "제조사명", example = "한국제약")
    private String entpName;

    @JsonProperty("image")
    @Schema(description = "약물 이미지 URL", example = "https://example.com/aspirin.png")
    private String image;

public TabooDto(String mixItemName, Long mixtureItemSeq, String prohbtContent, String className, String entpName) {
        this.mixItemName = mixItemName;
        this.mixtureItemSeq = mixtureItemSeq;
        this.prohbtContent = prohbtContent;
        this.className = className;
        this.entpName = entpName;
        this.image = null;
    }
}