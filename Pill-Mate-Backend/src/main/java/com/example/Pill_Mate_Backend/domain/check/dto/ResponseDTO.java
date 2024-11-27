package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseDTO {
    private List<MedicineDTO> medicineList;
    private WeekCountDTO weekCount;

    // 생성자, getter, setter
    public ResponseDTO(List<MedicineDTO> medicineList, WeekCountDTO weekCount) {
        this.medicineList = medicineList;
        this.weekCount = weekCount;
    }

    public List<MedicineDTO> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<MedicineDTO> medicineList) {
        this.medicineList = medicineList;
    }

    public WeekCountDTO getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(WeekCountDTO weekCount) {
        this.weekCount = weekCount;
    }
}
