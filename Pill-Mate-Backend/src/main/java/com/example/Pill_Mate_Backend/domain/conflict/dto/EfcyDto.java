package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

// v2
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EfcyDto {

    @JsonProperty("itemName")
    @Schema(description = "약물 이름", example = "아스피린정100mg")
    private String itemName;

    @JsonProperty("itemSeq")
    @Schema(description = "약물 식별 번호", example = "201101234")
    private String itemSeq;

    @JsonProperty("className")
    @Schema(description = "효능군 이름", example = "해열·진통·소염제")
    private String className;

    @JsonProperty("effectName")
    @Schema(description = "약물 종류", example = "진통제")
    private String effectName;

    @JsonProperty("entpName")
    @Schema(description = "제조사명", example = "한국제약")
    private String entpName;

    @JsonProperty("image")
    @Schema(description = "약물 이미지 URL", example = "https://example.com/aspirin.png")
    private String image;

    @Builder
    public EfcyDto(String itemName, String itemSeq, String className,
                   String effectName, String entpName) {
        this.itemName = itemName;
        this.itemSeq = itemSeq;
        this.className = className;
        this.effectName = effectName;
        this.entpName = entpName;
        this.image = null; // 이미지 없는 경우 null 처리
    }
}
