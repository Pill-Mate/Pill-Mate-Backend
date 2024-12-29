package com.example.Pill_Mate_Backend.domain.conflict.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsjntTabooApiItems {
    @JsonProperty("items")
    private List<UsjntTabooApiItem> items;

    @JsonCreator
    public UsjntTabooApiItems(@JsonProperty("body") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode itemsNode = node.findValue("items");
        this.items = Arrays.stream(objectMapper.treeToValue(itemsNode, UsjntTabooApiItem[].class)).toList();
    }
}
