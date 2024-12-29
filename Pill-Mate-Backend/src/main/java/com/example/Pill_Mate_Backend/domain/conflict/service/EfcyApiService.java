package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EfcyApiService {
    public EfcyDplctApiItems parseJson(String json) {
        EfcyDplctApiItems items = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            items = mapper.readValue(json, EfcyDplctApiItems.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public String efcyDplctProcessApiItems(String json) {
        EfcyDplctApiItems efcyDplctapiItems = parseJson(json);
        if (efcyDplctapiItems != null) {
            List<Map<String, String>> processedItems = new ArrayList<>();

            efcyDplctapiItems.getItems().forEach(item -> {
                Map<String, String> itemMap = new HashMap<>();
                itemMap.put("EFFECT_NAME", item.getEffectName());
                itemMap.put("ITEM_NAME", item.getItemName());
                itemMap.put("ITEM_SEQ", item.getItemSeq());
                processedItems.add(itemMap);
            });

            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(processedItems);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "{}";
    }

}
