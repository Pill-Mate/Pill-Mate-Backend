package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Hospital;
import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.Pharmacy;
import com.example.Pill_Mate_Backend.domain.conflict.dto.PhoneAddresses;
import com.example.Pill_Mate_Backend.domain.conflict.dto.TabooDto;
import com.example.Pill_Mate_Backend.domain.conflict.dto.UsjntTabooApiItem;
import com.example.Pill_Mate_Backend.domain.conflict.dto.UsjntTabooApiItems;
import com.example.Pill_Mate_Backend.domain.conflict.repository.DurTabooRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.HospitalRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.PharmacyRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.ScheduleRepository;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.handler.MedicineHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class TabooApiService {

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    private final MedicineRepository medicineRepository;
    private final HospitalRepository hospitalRepository;
    private final PharmacyRepository pharmacyRepository;
    private final DurTabooRepository durRepository;

    //병용금기 버전2( DB에서 직접 호출 )
    public List<TabooDto> tabooSearchWithUser(String itemSeq, String email){
        List<TabooDto> mixtureList = durRepository.findTabooByMixtureItemSeq(itemSeq);


        //병용 금기 약물 있는지 검색

        List<TabooDto> userHasList = new ArrayList<>();

        //사용자가 가지고 있으면 리스트에 추가
        for (TabooDto mixtureSeq : mixtureList) {
            if(medicineRepository.findByIdentifyNumberAndEmail(mixtureSeq.getMixtureItemSeq(),email).isPresent()) {

                mixtureSeq.setImage(medicineRepository.findMedicineImageByItemSeq(mixtureSeq.getMixtureItemSeq()));
                userHasList.add(mixtureSeq);
            }
        }
        return userHasList;
    }

    public List<String> tabooSearch(String itemSeq) {
        //병용 금기 약물 있는지 검색
        List<TabooDto> mixtureSeqList = durRepository.findTabooByMixtureItemSeq(itemSeq);

        List<String> itemSeqList = new ArrayList<>();

        //사용자가 가지고 있으면 리스트에 추가
        for (TabooDto mixtureSeq : mixtureSeqList) {
            itemSeqList.add(mixtureSeq.getMixtureItemSeq());

        }
            return itemSeqList;

    }

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

        // items가 비어있는 경우 처리
        if (usjntTabooApiItems == null || usjntTabooApiItems.getItems().isEmpty()) {
            System.out.println("No items found in the API response.");
            return "[]"; // 빈 응답 반환
        }

        // items가 있는 경우 처리
        List<Map<String, String>> processedItems = new ArrayList<>();

        usjntTabooApiItems.getItems().forEach(item -> {
            if(medicineRepository.findMedicineByIdentifyNumber(item.getMixtureItemSeq()) != null){
            System.out.println(medicineRepository.findMedicineByIdentifyNumber(item.getMixtureItemSeq()));
            //만약에 medicine에 해당 약물이 있으면 출력
            Map<String, String> itemMap = new HashMap<>();
            // 약물 데이터 처리
            itemMap.put("MIXTURE_ITEM_NAME", item.getMixtureItemName());
            itemMap.put("ITEM_SEQ", item.getMixtureItemSeq());
            itemMap.put("PROHBT_CONTENT", item.getProhbtContent());
            itemMap.put("ENTP_NAME", item.getEntpName());
            itemMap.put("CLASS_NAME", item.getClassName());
            Medicine medicine = medicineRepository.findMedicineByIdentifyNumber(item.getMixtureItemSeq());
            itemMap.put("ITEM_IMAGE", String.valueOf(medicine.getMedicineImage()));
            processedItems.add(itemMap);
        }
        });

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(processedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
            return "[]";
    }


        public PhoneAddresses getPhoneAddresses(String itemSeq,String email) {
            Long medicineId = medicineRepository.findMedicineIdByIdentifyNumberAndEmail(itemSeq,email);
            Pharmacy pharmacy = pharmacyRepository.findByMedicineId(medicineId);
            Hospital hospital = hospitalRepository.findByMedicineId(medicineId);
            PhoneAddresses phoneAddresses = new PhoneAddresses(
                    pharmacy.getPharmacyName(),
                    pharmacy.getPharmacyAddress(),
                    pharmacy.getPharmacyPhone(),
                    hospital.getHospitalName(),
                    hospital.getHospitalAddress(),
                    hospital.getHospitalPhone()
            );
            return phoneAddresses;

        }
    public List<UsjntTabooApiItem> getUsjntItemsFromDur(String itemSeq) {
        try {
            String url = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getUsjntTabooInfoList03?" +
                    "serviceKey=" + serviceKey +
                    "&pageNo=1" +
                    "&numOfRows=20" +
                    "&type=json" +
                    "&typeName=" + URLEncoder.encode("병용금기", StandardCharsets.UTF_8) +
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
            UsjntTabooApiItems result = mapper.readValue(sb.toString(), UsjntTabooApiItems.class);
            return result.getItems();

        } catch (Exception e) {
            log.error("병용금기 DUR API 호출 실패", e);
            return List.of(); // 비어 있는 리스트 반환
        }
    }
    public String getMaterialNameFromDur(String itemSeq) {
        try {
            String url = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getDurPrdlstInfoList03?" +
                    "serviceKey=" + serviceKey +
                    "&type=json" +
                    "&itemSeq=" + URLEncoder.encode(itemSeq, StandardCharsets.UTF_8);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());

            JsonNode items = root.path("body").path("items");

            // ✅ items 배열이 없거나 비어있는 경우 null 반환
            if (!items.isArray() || items.size() == 0) {
                return null;
            }

            // ✅ INGR_NAME 필드가 없어도 null 반환
            JsonNode ingrNameNode = items.get(0).path("INGR_NAME");
            if (ingrNameNode.isMissingNode() || ingrNameNode.isNull()) {
                return null;
            }

            return ingrNameNode.asText();

        } catch (Exception e) {
            log.error("getMaterialNameFromDur 실패: itemSeq=" + itemSeq, e);
            return null;
        }
    }



    public void delete(String itemSeq, String email) {
        Medicine medicine = medicineRepository.findByIdentifyNumberAndEmail(itemSeq,email)
                .orElseThrow(() -> new MedicineHandler(ErrorStatus._MEDICINE_NOT_FOUND));
        medicineRepository.delete(medicine);
    }
    public void deletePharmacy(Long pharmacyId){
        Optional<Pharmacy> optionalPharmacy = pharmacyRepository.findById(pharmacyId);
        Pharmacy pharmacy = optionalPharmacy.get();
        pharmacyRepository.delete(pharmacy);
    }
}
