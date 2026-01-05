package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//v2
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineConflict {

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("item_seq")
    private Long itemSeq;

    @JsonProperty("effect_name")
    private String effectName;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("entp_name")
    private String entpName;

    @JsonProperty("item_image")
    private String itemImage;
}
