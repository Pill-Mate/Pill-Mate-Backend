package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseDTO {
    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private Integer countAll;
    private Integer countLeft;
    private List<MedicineDTO> medicineList;

    // 생성자, getter, setter
    public List<MedicineDTO> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<MedicineDTO> medicineList) {
        this.medicineList = medicineList;
    }
}
