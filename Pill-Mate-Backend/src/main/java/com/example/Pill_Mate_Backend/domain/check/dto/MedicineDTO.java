package com.example.Pill_Mate_Backend.domain.check.dto;

import com.example.Pill_Mate_Backend.CommonEntity.enums.EatUnit;
//import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeSpecific;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.sql.Time;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDTO {//interface는 왜 안된걸까..? enum과 uri 때문에 안된듯.

    //medicine_schedule
    private Long medicineScheduleId;
    private String intakeCount;//intakespecific
    private Time intakeTime;
    private Integer eatCount;
    private String eatUnit;//eatunit
    private Integer mealTime;
    private String mealUnit;//mealunit
    private Boolean eatCheck;

    //medicine
    private String medicineName;
    private String medicineImage;
}
