package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItems;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 효능군 중복
@Slf4j
@Service
@RequiredArgsConstructor // Lombok을 이용한 자동 생성자 주입
public class EfcyApiService {

    private final MedicineRepository medicineRepository; // final 추가하여 불변성 유지

    public EfcyDplctApiItems parseJson(String json) {
        EfcyDplctApiItems items = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            items = mapper.readValue(json, EfcyDplctApiItems.class);
        } catch (Exception e) {
            log.error("JSON Parsing Error", e);
        }
        return items;
    }

    // 효능군 중복
    public String efcyDplctProcessApiItems(String json) {
        EfcyDplctApiItems efcyDplctapiItems = parseJson(json);

        if (efcyDplctapiItems == null || efcyDplctapiItems.getItems().isEmpty()) {
            log.info("No items found in the API response.");
            return "[]"; // 빈 응답 반환
        }

        List<Map<String, String>> processedItems = new ArrayList<>();

        efcyDplctapiItems.getItems().forEach(item -> {
            // 만약 해당 약물이 테이블에 있으면 JSON에 추가
            if (medicineRepository.findMedicineByIdentifyNumber(item.getItemSeq()) != null) {
                Map<String, String> itemMap = new HashMap<>();
                itemMap.put("ITEM_NAME", item.getItemName());
                itemMap.put("ITEM_SEQ", item.getItemSeq());
                itemMap.put("EFFECT_NAME", item.getEffectName());
                itemMap.put("CLASS_NAME", item.getClassName());
                itemMap.put("ENTP_NAME", item.getEntpName());
                Medicine medicine = medicineRepository.findMedicineByIdentifyNumber(item.getItemSeq());
                itemMap.put("ITEM_IMAGE", String.valueOf(medicine.getMedicineImage()));

                processedItems.add(itemMap);
            }
        });

        try {
            return new ObjectMapper().writeValueAsString(processedItems);
        } catch (JsonProcessingException e) {
            log.error("JSON Processing Error", e);
            return "[]";
        }
    }
}
