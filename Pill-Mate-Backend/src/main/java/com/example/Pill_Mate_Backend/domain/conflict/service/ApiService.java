package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Hospital;
import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.Pharmacy;
import com.example.Pill_Mate_Backend.domain.conflict.dto.PhoneAddresses;
import com.example.Pill_Mate_Backend.domain.conflict.dto.UsjntTabooApiItems;
import com.example.Pill_Mate_Backend.domain.register.repository.HospitalRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineScheduleRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.PharmacyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

//병용금기
@Slf4j
@Service
public class ApiService {
    private final MedicineRepository medicineRepository;
    private final HospitalRepository hospitalRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineScheduleRepository medicineScheduleRepository;

    public ApiService(MedicineRepository medicineRepository, HospitalRepository hospitalRepository, PharmacyRepository pharmacyRepository, MedicineScheduleRepository medicineScheduleRepository) {
        this.medicineRepository = medicineRepository;
        this.hospitalRepository = hospitalRepository;
        this.pharmacyRepository = pharmacyRepository;

        // 로그 출력
        System.out.println("MedicineRepository is " + (medicineRepository == null ? "NULL" : "NOT NULL"));
        System.out.println("HospitalRepository is " + (hospitalRepository == null ? "NULL" : "NOT NULL"));
        System.out.println("PharmacyRepository is " + (pharmacyRepository == null ? "NULL" : "NOT NULL"));
        this.medicineScheduleRepository = medicineScheduleRepository;
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


        public PhoneAddresses getPhoneAddresses(String itemSeq) {
            Long medicineId = medicineRepository.findMedicineIdByIdentifyNumber(itemSeq);
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

    public void delete(String itemSeq) {
        Medicine medicine = medicineRepository.findMedicineByIdentifyNumber(itemSeq);
        medicineRepository.delete(medicine);
    }
    public void deletePharmacy(Long pharmacyId){
        Optional<Pharmacy> optionalPharmacy = pharmacyRepository.findById(pharmacyId);
        Pharmacy pharmacy = optionalPharmacy.get();
        pharmacyRepository.delete(pharmacy);
    }
}
