package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.domain.conflict.dto.UsjntTabooApiItem;
import com.example.Pill_Mate_Backend.domain.conflict.dto.UsjntTabooApiItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//병용금기
@Service
public class ApiService {

    public UsjntTabooApiItems parseJson(String json) {
        UsjntTabooApiItems items = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            items = mapper.readValue(json, UsjntTabooApiItems.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    //병용금기
public String usjntTabooProcessApiItems(String json) {
    UsjntTabooApiItems usjntTabooApiItems = parseJson(json);
    if (usjntTabooApiItems != null) {
        List<Map<String, String>> processedItems = new ArrayList<>();

        usjntTabooApiItems.getItems().forEach(item -> {
            Map<String, String> itemMap = new HashMap<>();
            //약물 이름 ex) 로엘디정(심바스타틴)(수출용)
            itemMap.put("MIXTURE_ITEM_NAME", item.getMixtureItemName());
            //약물 번호
            itemMap.put("MIXTURE_ITEM_SEQ", item.getMixtureItemSeq());
            //병용금기 사유 ex)횡문근융해증
            itemMap.put("PROHBT_CONTENT", item.getProhbtContent());
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
