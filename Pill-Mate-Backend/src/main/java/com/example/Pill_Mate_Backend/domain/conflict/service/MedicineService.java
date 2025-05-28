package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.domain.conflict.dto.MedicineConflict;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.handler.MedicineHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;

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
}
