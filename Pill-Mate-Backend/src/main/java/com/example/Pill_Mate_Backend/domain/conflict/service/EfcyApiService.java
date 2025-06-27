package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.openApi.DurEffDuplication;
import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItem;
import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItems;
import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDto;
import com.example.Pill_Mate_Backend.domain.conflict.repository.DurEffDuplicationRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 효능군 중복
@Slf4j
@Service
@RequiredArgsConstructor // Lombok을 이용한 자동 생성자 주입
public class EfcyApiService {
    @Value("${openApi.serviceKey}")
    private String serviceKey;

    private final MedicineRepository medicineRepository;
    private final DurEffDuplicationRepository durEffDuplicationRepository;


    //효능군 중복 버전2( DB에서 직접 호출 )
    public List<EfcyDto> efcySearchWithUser(String itemSeq, String email) {
        DurEffDuplication entity = durEffDuplicationRepository.findByItemSeq(itemSeq);
        String durSeq = "";
        List<EfcyDto> itemSeqList;

        List<EfcyDto> efcyDtoList = new ArrayList<>();
        if (entity != null) {
            durSeq = entity.getDurSeq();
            itemSeqList = durEffDuplicationRepository.findEfcyByDurSeq(durSeq);
            for (EfcyDto dto : itemSeqList) {
                if (medicineRepository.findByIdentifyNumberAndEmail(dto.getItemSeq(), email).isPresent()) {
                    Medicine medicine = medicineRepository.findByIdentifyNumberAndEmail(dto.getItemSeq(), email).orElseThrow();
                    efcyDtoList.add(EfcyDto.builder()
                            .className(medicine.getClassName())
                            //추후 effectname으로 수정
                            .effectName(dto.getEffectName())
                            .entpName(medicine.getEntpName())
                            .itemName(medicine.getMedicineName())
                            .itemSeq(dto.getItemSeq())
                            .build());
                }
            }

        }
            return efcyDtoList;

        //사용자가 가지고 있으면 리스트에 추가


    }

    //효능군 중복 itemSeq리스트 리턴
    public List<String> efcySearch(String itemSeq) {
        DurEffDuplication entity = durEffDuplicationRepository.findByItemSeq(itemSeq);
        String durSeq = "";
        List<String> itemSeqList = new ArrayList<>();

        List<EfcyDto> efcyDtoList = new ArrayList<>();
        if (entity != null) {
            durSeq = entity.getDurSeq();
            itemSeqList = durEffDuplicationRepository.findAllByDurSeq(durSeq);

        }
        return itemSeqList;

        //사용자가 가지고 있으면 리스트에 추가


    }


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

    public List<EfcyDplctApiItem> getEfcyItemsFromDur(String itemSeq) {
        try {
            String url = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getEfcyDplctInfoList03?" +
                    "serviceKey=" + serviceKey +
                    "&pageNo=1" +
                    "&numOfRows=20" +
                    "&type=json" +
                    "&typeName=" + URLEncoder.encode("효능군중복", StandardCharsets.UTF_8) +
                    "&itemSeq=" + URLEncoder.encode(itemSeq, StandardCharsets.UTF_8);

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            connection.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            EfcyDplctApiItems result = mapper.readValue(sb.toString(), EfcyDplctApiItems.class);
            return result.getItems();

        } catch (Exception e) {
            log.error("효능군 중복 DUR API 호출 실패", e);
            return List.of(); // 비어 있는 리스트 반환
        }
    }

}
