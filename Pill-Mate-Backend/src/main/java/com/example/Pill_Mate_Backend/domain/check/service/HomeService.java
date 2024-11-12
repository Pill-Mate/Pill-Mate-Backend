package com.example.Pill_Mate_Backend.domain.check.service;

import com.example.Pill_Mate_Backend.CommonEntity.enums.EatUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeSpecific;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;

    //public List<MedicineDTO> getMedicineSchedulesByDate(String email, Date date) {
    //    System.out.print(medicineScheduleRepository.findByIntakeDate(email, date));//삭제
    //    return medicineScheduleRepository.findByIntakeDate(email, date);
    //}

    @SneakyThrows
    public List<MedicineDTO> getMedicineSchedulesByDate(String email, Date date) {
        List<Object[]> results = medicineScheduleRepository.findByIntakeDate(email, date);
        List<MedicineDTO> medicineDTOList = new ArrayList<>();

        for (Object[] result : results) {
            MedicineDTO dto = new MedicineDTO(
                    (Long) result[0],
                    (String) result[1],
                    (Time) result[2],
                    (Integer) result[3],
                    (String) result[4],
                    (Integer) result[5],
                    (String) result[6],
                    (Boolean) result[7],
                    (String) result[8],
                    result[9] instanceof byte[] ? new URI(new String((byte[]) result[9])) : null // 바이너리 데이터를 String으로 변환 후 URI로 파싱
            );
            medicineDTOList.add(dto);
        }

        return medicineDTOList;
    }
}
