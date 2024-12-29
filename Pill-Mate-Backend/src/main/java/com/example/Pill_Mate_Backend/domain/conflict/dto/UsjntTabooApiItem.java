package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsjntTabooApiItem {
    @JsonProperty("MIXTURE_ITEM_NAME")
    private String mixtureItemName;

    @JsonProperty("MIXTURE_ITEM_SEQ")
    private String mixtureItemSeq;

    @JsonProperty("PROHBT_CONTENT")
    private String prohbtContent;
}