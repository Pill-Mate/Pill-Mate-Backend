package com.example.Pill_Mate_Backend.domain.search.service;

import com.example.Pill_Mate_Backend.domain.conflict.service.MedicineService;
import com.example.Pill_Mate_Backend.domain.register.repository.DrugBasicRepository;
import com.example.Pill_Mate_Backend.domain.search.dto.PillSimpleDto;
import com.example.Pill_Mate_Backend.domain.search.dto.SearchPillResponseDto;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.handler.MedicineHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SearchService {
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private DrugBasicRepository drugBasicRepository;


    public SearchPillResponseDto searchPillDetail(Long itemSeq, String email) {
        PillSimpleDto dto = drugBasicRepository.findByItemSeq(itemSeq).orElseThrow(()->new MedicineHandler(ErrorStatus._MEDICINE_NOT_FOUND));
        return SearchPillResponseDto.builder()
                .itemImage(dto.getItemImage())
                .className(dto.getClassName())
                .itemName(dto.getItemName())
                .entpName(dto.getEntpName())
                .depositMethod(dto.getDepositMethod())
                .efcyQesitm(dto.getEfcyQesitm())
                .atpnQesitm(dto.getAtpnQesitm())
                .typeName(dto.getTypeName())
                .useMethodQesitm(dto.getUseMethodQesitm())
                .allConflictResponse(medicineService.checkAllConflicts(String.valueOf(itemSeq), email))
                .build();
    }




}
