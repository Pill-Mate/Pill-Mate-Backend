package com.example.Pill_Mate_Backend.domain.check.dto;

import com.example.Pill_Mate_Backend.CommonEntity.enums.EatUnit;
//import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeSpecific;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.sql.Time;

@Builder
@Data
public class MedicineDTO {//interface는 왜 안된걸까..? enum과 uri 때문에 안된듯.
    /*
    Long getMedicinescheduleid();
    IntakeSpecific getIntakespecific();
    Time getIntaketime();
    Integer getEatcount();
    EatUnit getEatunit();
    Integer getMealtime();
    MealUnit getMealunit();
    Boolean getEatcheck();

    //medicine
    String getMedicinename();
    URI getMedicineimage();
*/
    //medicine_schedule
    private Long medicineScheduleId;
    private String intakeSpecific;//intakespecific
    private Time intakeTime;
    private Integer eatCount;
    private String eatUnit;//eatunit
    private Integer mealTime;
    private String mealUnit;//mealunit
    private Boolean eatCheck;

    //medicine
    private String medicineName;
    private URI medicineImage;
    public MedicineDTO(Long id, String intakeSpecific, Time intakeTime, Integer eatCount,
                       String eatUnit, Integer mealTime, String mealUnit, Boolean eatCheck,
                       String medicineName, URI medicineImage) {
        // 필드 초기화
        setMedicineScheduleId(id);
        this.intakeSpecific = intakeSpecific;
        setIntakeTime(intakeTime);
        setEatCount(eatCount);
        this.eatUnit = eatUnit;
        setMealTime(mealTime);
        this.mealUnit = mealUnit;
        setEatCheck(eatCheck);
        setMedicineName(medicineName);
        setMedicineImage(medicineImage);
    }
}
