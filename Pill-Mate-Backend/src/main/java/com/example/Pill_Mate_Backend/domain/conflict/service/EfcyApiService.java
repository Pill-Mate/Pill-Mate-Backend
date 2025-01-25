package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//효능군중복
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
//효능군 중복
    public String efcyDplctProcessApiItems(String json) {
        EfcyDplctApiItems efcyDplctapiItems = parseJson(json);
        if (efcyDplctapiItems != null) {
            List<Map<String, String>> processedItems = new ArrayList<>();

            efcyDplctapiItems.getItems().forEach(item -> {
                Map<String, String> itemMap = new HashMap<>();
                //약물 종류 ex) 해열진통소염제
                itemMap.put("EFFECT_NAME", item.getEffectName());
                //약물 번호
                itemMap.put("ITEM_NAME", item.getItemName());
                //약물 성분 ex) 다이아펜정400밀리그램(이부브로펜)
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
