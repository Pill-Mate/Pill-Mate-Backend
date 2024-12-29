package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EfcyDplctApiItem {
    @JsonProperty("EFFECT_NAME")
    private String effectName;

    @JsonProperty("ITEM_NAME")
    private String itemName;

    @JsonProperty("ITEM_SEQ")
    private String itemSeq;
}
