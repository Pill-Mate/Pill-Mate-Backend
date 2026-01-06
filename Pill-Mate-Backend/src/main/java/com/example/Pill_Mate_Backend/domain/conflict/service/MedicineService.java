package com.example.Pill_Mate_Backend.domain.conflict.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.openApi.DurEffDuplication;
import com.example.Pill_Mate_Backend.domain.conflict.dto.*;
import com.example.Pill_Mate_Backend.domain.conflict.repository.DurEffDuplicationRepository;
import com.example.Pill_Mate_Backend.domain.conflict.repository.DurTabooRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final TabooApiService tabooApiService;
    private final EfcyApiService efcyApiService;
    private final DurTabooRepository durTabooRepository;
    private final DurEffDuplicationRepository durEffDuplicationRepository;

    public MedicineConflict findAll(Long itemSeq, String email) {
       Medicine medicine =  medicineRepository.findByItemSeqAndEmail(itemSeq,email)
               .orElse(null);

        if (medicine == null) {
            return null; // or throw new MedicineHandler(...)
        }

       return MedicineConflict.builder()
               .className(medicine.getClassName())
               .effectName(medicine.getEfficacy())
               .itemImage(medicine.getMedicineImage().toString())
               .entpName(medicine.getEntpName())
               .itemSeq(medicine.getItemSeq())
               .itemName(medicine.getMedicineName())
               .build();



    }
    @Transactional
    public AllConflictResponse checkAllConflicts(Long itemSeq, String email) {
        List<TabooDto> usjntList = new ArrayList<>();
        List<EfcyDto> efcyList = new ArrayList<>();

        List<TabooDto> mixtureList = durTabooRepository.findTabooByMixtureItemSeq(itemSeq);

        for (TabooDto mixtureSeq : mixtureList) {
            if (medicineRepository.findByItemSeqAndEmail(mixtureSeq.getMixtureItemSeq(), email).isPresent()) {
                String image = medicineRepository.findMedicineImageByItemSeq(mixtureSeq.getMixtureItemSeq());
                mixtureSeq.setImage(image); // null-safe
                usjntList.add(mixtureSeq);
            }
        }


        DurEffDuplication entity = durEffDuplicationRepository.findByItemSeq(itemSeq);
        String durSeq = "";
        List<EfcyDto> itemSeqList;

        if (entity != null) {
            durSeq = entity.getDurSeq();
            itemSeqList = durEffDuplicationRepository.findEfcyByDurSeq(durSeq);
            for (EfcyDto dto : itemSeqList) {
                if (medicineRepository.findByItemSeqAndEmail(dto.getItemSeq(), email).isPresent()) {
                    Medicine medicine = medicineRepository.findByItemSeqAndEmail(dto.getItemSeq(), email).orElseThrow();

                    String image = medicine.getMedicineImage() != null
                            ? medicine.getMedicineImage().toString()
                            : null;
                    EfcyDto efcy = EfcyDto.builder()
                            .className(medicine.getClassName())
                            //추후 effectname으로 수정
                            .effectName(dto.getEffectName())
                            .entpName(medicine.getEntpName())
                            .itemName(medicine.getMedicineName())
                            .itemSeq(dto.getItemSeq())
                            .build();
                    efcy.setImage(image);
                    efcyList.add(efcy);
                }
            }

        }

        MedicineConflict medicineConflict = findAll(itemSeq, email);

        return AllConflictResponse.builder()
                .usjntTabooList(usjntList)
                .efcyDplctList(efcyList)
                .conflictWithUserMeds(medicineConflict)
                .build();
    }


    private List<MedicineConflict> findUserConflicts(String email,
                                                     List<Long> usjntList,
                                                     List<Long> efcyList) {

        List<Long> myItemSeqs = medicineRepository.findAllByEmail(email)
                .stream()
                .map(m -> m.getItemSeq())
                .toList();

        Set<Long> conflictSeqs = new HashSet<>();
        usjntList.forEach(item -> {
            if (myItemSeqs.contains(item)) {
                conflictSeqs.add(item);
            }
        });

        efcyList.forEach(item -> {
            if (myItemSeqs.contains(item)) {
                conflictSeqs.add(item);
            }
        });

            return medicineRepository.findAllByItemSeqInAndUserEmail(conflictSeqs,email)
                .stream()
                .map(med -> MedicineConflict.builder()
                        .itemName(med.getMedicineName())
                        .itemSeq(med.getItemSeq())
                        .effectName(med.getEfficacy())
                        .className(med.getClassName())
                        .entpName(med.getEntpName())
                        .itemImage(med.getMedicineImage().toString())
                        .build())
                .toList();
    }


}
