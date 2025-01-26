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
public class EfcyDplctApiItems {
    @JsonProperty("items")
    private List<EfcyDplctApiItem> items;

    @JsonCreator
    public EfcyDplctApiItems(@JsonProperty("body") JsonNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode itemsNode = node.findValue("items");
        if (itemsNode != null && itemsNode.isArray()) {
            this.items = Arrays.stream(objectMapper.treeToValue(itemsNode, EfcyDplctApiItem[].class)).toList();
        } else {
            this.items = List.of(); // 빈 리스트로 초기화
            handleEmptyItems(); // items가 비어 있는 경우 처리
        }
    }

        /**
         * items가 비어 있는 경우 추가적인 처리를 수행합니다.
         */
        private void handleEmptyItems() {
            System.out.println("No items found in the API response."); // 로그 출력
            // 추가 동작 (예: 예외 발생, 디폴트 값 설정, 다른 동작 수행 등)
            // throw new IllegalStateException("Items cannot be empty."); // 예외를 던지는 경우
        }
    }

