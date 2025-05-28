package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineConflict {

    @JsonProperty("ITEM_NAME")
    private String ITEM_NAME;

    @JsonProperty("ITEM_SEQ")
    private String ITEM_SEQ;

    @JsonProperty("EFFECT_NAME")
    private String EFFECT_NAME;

    @JsonProperty("CLASS_NAME")
    private String CLASS_NAME;

    @JsonProperty("ENTP_NAME")
    private String ENTP_NAME;

    @JsonProperty("ITEM_IMAGE")
    private String ITEM_IMAGE;
}
