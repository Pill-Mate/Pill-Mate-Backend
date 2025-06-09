package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.domain.conflict.dto.AllConflictResponse;
import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDplctApiItem;
import com.example.Pill_Mate_Backend.domain.conflict.dto.MedicineConflict;
import com.example.Pill_Mate_Backend.domain.conflict.dto.UsjntTabooApiItem;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.handler.MedicineHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final ApiService apiService;
    private final EfcyApiService efcyApiService;

    public MedicineConflict findAll(String itemSeq, String email) {
       Medicine medicine =  medicineRepository.findByIdentifyNumberAndEmail(itemSeq,email)
               .orElseThrow(()->new MedicineHandler(ErrorStatus._MEDICINE_NOT_FOUND));

       return MedicineConflict.builder()
               .CLASS_NAME(medicine.getClassName())
               .EFFECT_NAME(medicine.getEfficacy())
               .ITEM_IMAGE(medicine.getMedicineImage().toString())
               .ENTP_NAME(medicine.getEntpName())
               .ITEM_SEQ(medicine.getIdentifyNumber())
               .ITEM_NAME(medicine.getMedicineName())
               .build();



    }
    public AllConflictResponse checkAllConflicts(String itemSeq, String email) {
        //String materialName = apiService.getMaterialNameFromDur(itemSeq); // 새로 구현 필요

        List<UsjntTabooApiItem> usjntList = apiService.getUsjntItemsFromDur(itemSeq);
        List<EfcyDplctApiItem> efcyList = efcyApiService.getEfcyItemsFromDur(itemSeq);

        List<MedicineConflict> userConflicts = findUserConflicts(email, usjntList, efcyList);

        return AllConflictResponse.builder()
                .usjntTabooList(usjntList)
                .efcyDplctList(efcyList)
                .conflictWithUserMeds(userConflicts)
                .build();
    }


    private List<MedicineConflict> findUserConflicts(String email,
                                                     List<UsjntTabooApiItem> usjntList,
                                                     List<EfcyDplctApiItem> efcyList) {

        List<String> myItemSeqs = medicineRepository.findAllByEmail(email)
                .stream()
                .map(m -> m.getIdentifyNumber())
                .toList();

        Set<String> conflictSeqs = new HashSet<>();
        usjntList.forEach(item -> {
            if (myItemSeqs.contains(item.getMixtureItemSeq())) {
                conflictSeqs.add(item.getMixtureItemSeq());
            }
        });

        efcyList.forEach(item -> {
            if (myItemSeqs.contains(item.getItemSeq())) {
                conflictSeqs.add(item.getItemSeq());
            }
        });

            return medicineRepository.findAllByIdentifyNumberInAndUserEmail(conflictSeqs,email)
                .stream()
                .map(med -> MedicineConflict.builder()
                        .ITEM_NAME(med.getMedicineName())
                        .ITEM_SEQ(med.getIdentifyNumber())
                        .EFFECT_NAME(med.getEfficacy())
                        .CLASS_NAME(med.getClassName())
                        .ENTP_NAME(med.getEntpName())
                        .ITEM_IMAGE(med.getMedicineImage().toString())
                        .build())
                .toList();
    }


}
