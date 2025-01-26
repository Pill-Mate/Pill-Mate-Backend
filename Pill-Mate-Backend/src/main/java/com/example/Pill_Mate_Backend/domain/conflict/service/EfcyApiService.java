package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItems;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//효능군중복
@Slf4j
@Service
public class EfcyApiService {

    private final MedicineRepository medicineRepository;

    public EfcyApiService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

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

        // items가 비어있는 경우 처리
        if (efcyDplctapiItems == null || efcyDplctapiItems.getItems().isEmpty()) {
            System.out.println("No items found in the API response.");
            return "{}"; // 빈 응답 반환
        }
            List<Map<String, String>> processedItems = new ArrayList<>();
        if (efcyDplctapiItems != null) {

            efcyDplctapiItems.getItems().forEach(item -> {
                //만약 해당 약물이 테이블에 있으면 json에 추가
                if(medicineRepository.findMedicineByIdentifyNumber(item.getItemSeq()) != null) {
                    Map<String, String> itemMap = new HashMap<>();
                    //약물 번호
                    itemMap.put("ITEM_NAME", item.getItemName());
                    //약물 성분 ex) 다이아펜정400밀리그램(이부브로펜)
                    itemMap.put("ITEM_SEQ", item.getItemSeq());
                    //약물 분류명
                    itemMap.put("EFFECT_NAME", item.getEffectName());
                    //효능군 중복 내용 ex) 해열진통소염제
                    itemMap.put("CLASS_NAME", item.getClassName());

                    itemMap.put("ENTP_NAME", item.getEntpName());


                    processedItems.add(itemMap);
                }
            });

            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(processedItems);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "[{}]";
    }

}
