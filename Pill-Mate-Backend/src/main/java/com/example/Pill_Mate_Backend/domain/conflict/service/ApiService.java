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

//    public void processApiItems(String json) {
//        ApiItems durItems = parseJson(json);
//        if (durItems != null) {
//            durItems.getItems().forEach(item -> {
//                System.out.println("MIXTURE_ITEM_NAME: " + item.getMixtureItemName());
//                System.out.println("MIXTURE_ITEM_SEQ: " + item.getMixtureItemSeq());
//                System.out.println("PROHBT_CONTENT: " + item.getProhbtContent());
//                System.out.println();
//            });
public String usjntTabooProcessApiItems(String json) {
    UsjntTabooApiItems usjntTabooApiItems = parseJson(json);
    if (usjntTabooApiItems != null) {
        List<Map<String, String>> processedItems = new ArrayList<>();

        usjntTabooApiItems.getItems().forEach(item -> {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("MIXTURE_ITEM_NAME", item.getMixtureItemName());
            itemMap.put("MIXTURE_ITEM_SEQ", item.getMixtureItemSeq());
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
