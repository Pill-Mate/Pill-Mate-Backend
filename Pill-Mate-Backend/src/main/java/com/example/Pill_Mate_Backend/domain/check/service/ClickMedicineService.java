package com.example.Pill_Mate_Backend.domain.check.service;

import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDetailDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Arrays;

import static org.hibernate.type.SqlTypes.JSON;


@RequiredArgsConstructor
@Service
public class ClickMedicineService {
    @Autowired
    private MedicineScheduleRepository2 medicineScheduleRepository2;
    public MedicineDetailDTO getMedicineDetailByScheduleId(Long medicineScheduleId){
        Object[] result = medicineScheduleRepository2.findMedicineDetailByScheduleId(medicineScheduleId);

        for (Object obj : result) { //내용 확인용 출력 삭제해도 됨
            // 내부 배열 요소를 하나씩 출력
            Object[] innerArray = (Object[]) obj;
            System.out.println("Inner Array:");
            for (Object innerObj : innerArray) {
                System.out.println("Element: " + innerObj);
            };
            //uri만 처리 후 출력
            byte[] byteArray = (byte[]) innerArray[1];
            String stringData = new String(byteArray);
            URI uri = URI.create(stringData);
            System.out.println("uri:"+uri);
        }

        String medicineName = null;
        URI medicineImage= null;
        String className= null;
        String ingredient= null;
        String efficacy= null;
        String caution= null;
        String sideEffect= null;
        String storage= null;
        String entpName = null;

        for (Object obj : result) {
            // 내부 배열 요소를 하나씩 출력
            Object[] innerArray = (Object[]) obj;
            medicineName = (String) innerArray[0];
            //uri..
            byte[] byteArray = (byte[]) innerArray[1];
            String stringData = new String(byteArray);
            medicineImage = URI.create(stringData);
            className = (String) innerArray[2];
            ingredient = (String) innerArray[3];
            efficacy = (String) innerArray[4];
            caution = (String) innerArray[5];
            sideEffect = (String) innerArray[6];
            storage = (String) innerArray[7];
            entpName = (String) innerArray[8];
        }

        // DTO에 값 설정
        MedicineDetailDTO medicineDetailDTO = new MedicineDetailDTO(
                medicineName,
                medicineImage,
                className,
                ingredient,
                efficacy,
                caution,
                sideEffect,
                storage,
                entpName
        );
        return medicineDetailDTO;
    }
}
